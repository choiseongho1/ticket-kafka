package com.study.ticket.domain.payment.controller.dto;

import com.study.ticket.domain.payment.domain.entity.Payment;
import com.study.ticket.domain.payment.domain.enums.PaymentMethod;
import com.study.ticket.domain.payment.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 결제 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    /**
     * 결제 ID
     */
    private Long id;

    /**
     * 결제 번호
     */
    private String paymentNumber;

    /**
     * 주문 ID
     */
    private Long orderId;

    /**
     * 주문 번호
     */
    private String orderNumber;

    /**
     * 결제 금액
     */
    private Integer amount;

    /**
     * 결제 방법
     */
    private PaymentMethod method;

    /**
     * 결제 상태
     */
    private PaymentStatus status;

    /**
     * 외부 결제 시스템 키
     */
    private String paymentKey;

    /**
     * 결제 시간
     */
    private LocalDateTime paymentTime;

    /**
     * 취소 시간
     */
    private LocalDateTime cancelTime;

    /**
     * 취소 사유
     */
    private String cancelReason;

    /**
     * 생성 시간
     */
    private LocalDateTime createdAt;

    /**
     * 결제 엔티티를 DTO로 변환합니다.
     * @param payment 결제 엔티티
     * @return 결제 응답 DTO
     */
    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentNumber(payment.getPaymentNumber())
                .orderId(payment.getOrder().getId())
                .orderNumber(payment.getOrder().getOrderNumber())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .paymentKey(payment.getPaymentKey())
                .paymentTime(payment.getPaymentTime())
                .cancelTime(payment.getCancelTime())
                .cancelReason(payment.getCancelReason())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
