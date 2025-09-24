package com.study.ticket.domain.payment.dto;

import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.payment.domain.entity.Payment;
import com.study.ticket.domain.payment.domain.enums.PaymentMethod;
import com.study.ticket.domain.payment.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 결제 생성 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSaveDto {

    /**
     * 주문 ID
     */
    private Long orderId;

    /**
     * 결제 방법
     */
    private PaymentMethod method;

    public Payment toEntity(Order order) {
        return Payment.builder()
            .order(order)
            .amount(order.getTotalAmount())
            .method(method)
            .status(PaymentStatus.PENDING)
            .build();
    }
}
