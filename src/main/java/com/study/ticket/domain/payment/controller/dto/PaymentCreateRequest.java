package com.study.ticket.domain.payment.controller.dto;

import com.study.ticket.domain.payment.domain.enums.PaymentMethod;
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
public class PaymentCreateRequest {

    /**
     * 주문 ID
     */
    private Long orderId;

    /**
     * 결제 방법
     */
    private PaymentMethod method;
}
