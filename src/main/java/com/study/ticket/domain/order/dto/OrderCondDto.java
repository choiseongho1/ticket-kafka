package com.study.ticket.domain.order.dto;

import com.study.ticket.domain.order.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 주문 조회조건 DTO
 */
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class OrderCondDto {

    /**
     * 주문 상태
     */
    private OrderStatus status;

}
