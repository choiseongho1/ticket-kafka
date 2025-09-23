package com.study.ticket.domain.screening.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 상영 생성 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningCreateRequest {

    /**
     * 영화 ID
     */
    private Long movieId;

    /**
     * 상영관 이름
     */
    private String screenName;

    /**
     * 상영 시작 시간
     */
    private LocalDateTime startTime;

    /**
     * 상영 종료 시간
     */
    private LocalDateTime endTime;

    /**
     * 총 좌석 수
     */
    private Integer totalSeats;

    /**
     * 티켓 가격
     */
    private Integer price;
}
