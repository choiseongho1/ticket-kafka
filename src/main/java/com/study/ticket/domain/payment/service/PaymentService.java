package com.study.ticket.domain.payment.service;

import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.order.domain.enums.OrderStatus;
import com.study.ticket.domain.order.domain.repository.OrderRepository;
import com.study.ticket.domain.order.service.OrderService;
import com.study.ticket.domain.outbox.service.OutboxEventService;
import com.study.ticket.domain.payment.domain.entity.Payment;
import com.study.ticket.domain.payment.domain.enums.PaymentMethod;
import com.study.ticket.domain.payment.domain.enums.PaymentStatus;
import com.study.ticket.domain.payment.domain.repository.PaymentRepository;
import com.study.ticket.domain.payment.event.PaymentApprovedEvent;
import com.study.ticket.domain.payment.event.PaymentCancelledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 결제 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final OutboxEventService outboxEventService;
    
    @Value("${kafka.topics.payment-events}")
    private String paymentEventsTopic;
    
    /**
     * 결제를 생성합니다.
     * @param orderId 주문 ID
     * @param method 결제 방법
     * @return 생성된 결제
     */
    @Transactional
    public Payment createPayment(Long orderId, PaymentMethod method) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없음: " + orderId));
        
        // 주문 상태 확인
        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.PAYMENT_PENDING) {
            throw new RuntimeException("결제 가능한 상태가 아닙니다: " + order.getStatus());
        }
        
        // 이미 결제가 있는지 확인
        if (paymentRepository.existsByOrder(order)) {
            throw new RuntimeException("이미 결제가 존재합니다: " + orderId);
        }
        
        // 결제 생성
        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .method(method)
                .status(PaymentStatus.PENDING)
                .build();
        
        // 주문 상태 변경
        orderService.waitForPayment(orderId);
        
        // 결제 저장
        return paymentRepository.save(payment);
    }
    
    /**
     * 결제를 조회합니다.
     * @param paymentId 결제 ID
     * @return 결제
     */
    @Transactional(readOnly = true)
    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("결제를 찾을 수 없음: " + paymentId));
    }
    
    /**
     * 주문에 대한 결제를 조회합니다.
     * @param orderId 주문 ID
     * @return 결제
     */
    @Transactional(readOnly = true)
    public Payment getPaymentByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없음: " + orderId));
        return paymentRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("결제를 찾을 수 없음, 주문 ID: " + orderId));
    }
    
    /**
     * 결제를 승인합니다.
     * @param paymentId 결제 ID
     * @param paymentKey 외부 결제 시스템 키
     * @return 승인된 결제
     */
    @Transactional
    public Payment approvePayment(Long paymentId, String paymentKey) {
        Payment payment = getPayment(paymentId);
        
        // 결제 상태 확인
        if (payment.getStatus() != PaymentStatus.PENDING && payment.getStatus() != PaymentStatus.PROCESSING) {
            throw new RuntimeException("승인 가능한 상태가 아닙니다: " + payment.getStatus());
        }
        
        // 결제 승인
        payment.approve(paymentKey);
        
        // 주문 상태 변경
        Order order = payment.getOrder();
        order.completePayment();
        orderRepository.save(order);
        
        // 결제 저장
        Payment savedPayment = paymentRepository.save(payment);
        
        // 결제 승인 이벤트 발행
        PaymentApprovedEvent event = new PaymentApprovedEvent(savedPayment);
        outboxEventService.saveEvent(
                event,
                "PAYMENT",
                savedPayment.getId().toString(),
                paymentEventsTopic
        );
        
        log.info("결제 승인 완료: {}", savedPayment.getPaymentNumber());
        return savedPayment;
    }
    
    /**
     * 결제를 취소합니다.
     * @param paymentId 결제 ID
     * @param reason 취소 사유
     * @return 취소된 결제
     */
    @Transactional
    public Payment cancelPayment(Long paymentId, String reason) {
        Payment payment = getPayment(paymentId);
        
        // 결제 상태 확인
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("취소 가능한 상태가 아닙니다: " + payment.getStatus());
        }
        
        // 결제 취소
        payment.cancel(reason);
        
        // 결제 저장
        Payment savedPayment = paymentRepository.save(payment);
        
        // 결제 취소 이벤트 발행
        PaymentCancelledEvent event = new PaymentCancelledEvent(savedPayment, reason);
        outboxEventService.saveEvent(
                event,
                "PAYMENT",
                savedPayment.getId().toString(),
                paymentEventsTopic
        );
        
        log.info("결제 취소 완료: {}", savedPayment.getPaymentNumber());
        return savedPayment;
    }
}
