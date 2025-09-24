package com.study.ticket.domain.order.service;

import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.order.domain.entity.OrderItem;
import com.study.ticket.domain.order.domain.enums.OrderStatus;
import com.study.ticket.domain.order.domain.repository.OrderRepository;
import com.study.ticket.domain.order.dto.OrderCondDto;
import com.study.ticket.domain.order.dto.OrderDetailDto;
import com.study.ticket.domain.order.dto.OrderListDto;
import com.study.ticket.domain.order.dto.OrderSaveDto;
import com.study.ticket.domain.order.event.OrderCancelledEvent;
import com.study.ticket.domain.order.event.OrderCreatedEvent;
import com.study.ticket.domain.outbox.service.OutboxEventService;
import com.study.ticket.domain.screening.domain.entity.Screening;
import com.study.ticket.domain.screening.domain.repository.ScreeningRepository;
import com.study.ticket.domain.user.domain.entity.User;
import com.study.ticket.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 주문 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ScreeningRepository screeningRepository;
    private final OutboxEventService outboxEventService;
    
    @Value("${kafka.topics.order-events}")
    private String orderEventsTopic;
    
    /**
     * 주문을 생성합니다.
     * @param OrderSaveDto orderSaveDto
     * @return 생성된 주문
     */
    @Transactional
    public void createOrder(OrderSaveDto orderSaveDto) {
        // 사용자 조회
        User user = userRepository.findById(orderSaveDto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음: " + orderSaveDto.getUserId()));
        
        // 상영 조회
        Screening screening = screeningRepository.findById(orderSaveDto.getScreeningId())
                .orElseThrow(() -> new RuntimeException("상영을 찾을 수 없음: " + orderSaveDto.getScreeningId()));
        
        // 좌석 예약 가능 여부 확인
        if (!screening.isAvailable(orderSaveDto.getSeatNumbers().size())) {
            throw new RuntimeException("좌석 예약 불가: 잔여 좌석 부족");
        }
        
        // 주문 생성
        Order order = orderSaveDto.toEntity(user, screening);
        
        // 좌석 예약
        screening.reserve(orderSaveDto.getSeatNumbers().size());
        
        // 주문 항목 추가
        for (String seatNumber : orderSaveDto.getSeatNumbers()) {
            OrderItem orderItem = OrderItem.builder()
                    .seatNumber(seatNumber)
                    .price(screening.getPrice())
                    .build();
            order.addOrderItem(orderItem);
        }
        
        // 주문 저장
        Order savedOrder = orderRepository.save(order);
        
        // 주문 생성 이벤트 발행
        OrderCreatedEvent event = new OrderCreatedEvent(savedOrder);
        outboxEventService.saveEvent(
                event,
                "ORDER",
                savedOrder.getId().toString(),
                orderEventsTopic
        );
        
        log.info("주문 생성 완료: {}", savedOrder.getOrderNumber());
    }
    
    /**
     * 주문을 조회합니다.
     * @param orderId 주문 ID
     * @return 주문
     */
    @Transactional(readOnly = true)
    public OrderDetailDto findOrderDetail(Long orderId) {
        return orderRepository.findOrderDetailByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없음: " + orderId));
    }
    
    /**
     * 주문 번호로 주문을 조회합니다.
     * @param orderNumber 주문 번호
     * @return 주문
     */
    @Transactional(readOnly = true)
    public OrderDetailDto findOrderDetailByOrderNumber(String orderNumber) {
        return orderRepository.findOrderDetailByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없음: " + orderNumber));
    }
    
    /**
     * 사용자의 주문 목록을 조회합니다.
     * @param userId 사용자 ID
     * @param OrderCondDto orderCondDto
     * @param pageable 페이지 정보
     * @return 주문 페이지
     */
    @Transactional(readOnly = true)
    public Page<OrderListDto> findOrderListWithPaging(Long userId, OrderCondDto orderCondDto, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음: " + userId));

        return orderRepository.findOrderListWithPaging(userId, orderCondDto, pageable);
    }

    /**
     * 주문을 취소합니다.
     * @param orderId 주문 ID
     * @return 취소된 주문
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문 정보를 찾을 수 없음: " + orderId));

        // 이미 취소된 주문인지 확인
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("이미 취소된 주문입니다: " + orderId);
        }

        // 완료된 주문은 취소할 수 없음
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("완료된 주문은 취소할 수 없습니다: " + orderId);
        }

        // 주문 취소
        order.cancel();

        // 좌석 예약 취소
        Screening screening = order.getScreening();
        screening.cancelReservation(order.getSeatCount());

        // 주문 취소 이벤트 발행
        OrderCancelledEvent event = new OrderCancelledEvent(order);
        outboxEventService.saveEvent(
                event,
                "ORDER",
                order.getId().toString(),
                orderEventsTopic
        );

        log.info("주문 취소 완료: {}", order.getOrderNumber());
        orderRepository.save(order);
    }

    
    /**
     * 주문을 결제 대기 상태로 변경합니다.
     * @param orderId 주문 ID
     */
    @Transactional
    public void waitForPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문 정보를 찾을 수 없음: " + orderId));;
        order.waitForPayment();
        orderRepository.save(order);
    }



    /**
     * 주문을 완료 상태로 변경합니다.
     * @param orderId 주문 ID
     * @return 완료된 주문
     */
    @Transactional
    public Order completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문 정보를 찾을 수 없음: " + orderId));;
        order.complete();
        return orderRepository.save(order);
    }
}
