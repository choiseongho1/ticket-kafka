package com.study.ticket.domain.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 주문 생성 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    /**
     * 사용자 ID
     */
    private Long userId;

    /**
     * 상영 ID
     */
    private Long screeningId;

    /**
     * 좌석 번호 목록
     */
    private List<String> seatNumbers;
}
