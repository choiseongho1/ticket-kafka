package com.study.ticket.domain.screening.dto;

import com.querydsl.core.annotations.QueryProjection;
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
public class ScreeningListDto {

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

    @QueryProjection
    public ScreeningListDto(Long id, Long movieId, String movieTitle, String screenName, LocalDateTime startTime, LocalDateTime endTime, Integer totalSeats, Integer availableSeats, Integer price) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.screenName = screenName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.price = price;
    }
}
