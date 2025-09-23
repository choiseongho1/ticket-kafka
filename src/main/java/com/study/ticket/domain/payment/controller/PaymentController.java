package com.study.ticket.domain.payment.controller;

import com.study.ticket.domain.payment.controller.dto.PaymentApproveRequest;
import com.study.ticket.domain.payment.controller.dto.PaymentCancelRequest;
import com.study.ticket.domain.payment.controller.dto.PaymentCreateRequest;
import com.study.ticket.domain.payment.controller.dto.PaymentResponse;
import com.study.ticket.domain.payment.domain.entity.Payment;
import com.study.ticket.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * 결제 컨트롤러
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제를 생성합니다.
     * @param request 결제 생성 요청
     * @return 생성된 결제
     */
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentCreateRequest request) {
        log.info("결제 생성 요청: {}", request);
        
        Payment payment = paymentService.createPayment(
                request.getOrderId(),
                request.getMethod()
        );
        
        PaymentResponse response = PaymentResponse.from(payment);
        return ResponseEntity
                .created(URI.create("/api/payments/" + payment.getId()))
                .body(response);
    }

    /**
     * 결제를 조회합니다.
     * @param paymentId 결제 ID
     * @return 결제
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long paymentId) {
        log.info("결제 조회 요청: {}", paymentId);
        
        Payment payment = paymentService.getPayment(paymentId);
        PaymentResponse response = PaymentResponse.from(payment);
        return ResponseEntity.ok(response);
    }

    /**
     * 결제를 승인합니다.
     * @param paymentId 결제 ID
     * @param request 결제 승인 요청
     * @return 승인된 결제
     */
    @PostMapping("/{paymentId}/approve")
    public ResponseEntity<PaymentResponse> approvePayment(
            @PathVariable Long paymentId,
            @RequestBody PaymentApproveRequest request
    ) {
        log.info("결제 승인 요청: {}, {}", paymentId, request);
        
        Payment payment = paymentService.approvePayment(
                paymentId,
                request.getPaymentKey()
        );
        
        PaymentResponse response = PaymentResponse.from(payment);
        return ResponseEntity.ok(response);
    }

    /**
     * 결제를 취소합니다.
     * @param paymentId 결제 ID
     * @param request 결제 취소 요청
     * @return 취소된 결제
     */
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<PaymentResponse> cancelPayment(
            @PathVariable Long paymentId,
            @RequestBody PaymentCancelRequest request
    ) {
        log.info("결제 취소 요청: {}, {}", paymentId, request);
        
        Payment payment = paymentService.cancelPayment(
                paymentId,
                request.getReason()
        );
        
        PaymentResponse response = PaymentResponse.from(payment);
        return ResponseEntity.ok(response);
    }
}
