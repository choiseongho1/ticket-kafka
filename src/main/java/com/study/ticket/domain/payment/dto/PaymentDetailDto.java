package com.study.ticket.domain.payment.dto;

import com.querydsl.core.annotations.QueryProjection;
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
public class PaymentDetailDto {

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

    @QueryProjection
    public PaymentDetailDto (Long id, String paymentNumber, Long orderId, String orderNumber, Integer amount, PaymentMethod method, PaymentStatus status, String paymentKey, LocalDateTime paymentTime, LocalDateTime cancelTime, String cancelReason, LocalDateTime createdAt){
        this.id = id;
        this.paymentNumber = paymentNumber;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.paymentKey = paymentKey;
        this.paymentTime = paymentTime;
        this.cancelTime = cancelTime;
        this.cancelReason = cancelReason;
        this.createdAt = createdAt;
    }
}
