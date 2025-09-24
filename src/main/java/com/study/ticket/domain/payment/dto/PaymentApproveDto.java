package com.study.ticket.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 결제 승인 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentApproveDto {

    /**
     * 외부 결제 시스템 키
     */
    private String paymentKey;
}
