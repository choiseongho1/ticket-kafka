package com.study.ticket.domain.screening.controller.dto;

import com.study.ticket.domain.screening.domain.entity.Screening;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 상영 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningResponse {

    /**
     * 상영 ID
     */
    private Long id;

    /**
     * 영화 ID
     */
    private Long movieId;

    /**
     * 영화 제목
     */
    private String movieTitle;

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
     * 가용 좌석 수
     */
    private Integer availableSeats;

    /**
     * 티켓 가격
     */
    private Integer price;

    /**
     * 상영 엔티티를 DTO로 변환합니다.
     * @param screening 상영 엔티티
     * @return 상영 응답 DTO
     */
    public static ScreeningResponse from(Screening screening) {
        return ScreeningResponse.builder()
                .id(screening.getId())
                .movieId(screening.getMovie().getId())
                .movieTitle(screening.getMovie().getTitle())
                .screenName(screening.getScreenName())
                .startTime(screening.getStartTime())
                .endTime(screening.getEndTime())
                .totalSeats(screening.getTotalSeats())
                .availableSeats(screening.getAvailableSeats())
                .price(screening.getPrice())
                .build();
    }
}
