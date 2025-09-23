package com.study.ticket.domain.screening.event;

import com.study.ticket.domain.screening.domain.entity.Screening;
import com.study.ticket.global.event.BaseEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 상영 업데이트 이벤트
 */
@Getter
@NoArgsConstructor
public class ScreeningUpdatedEvent extends BaseEvent {

    private static final String EVENT_TYPE = "SCREENING_UPDATED";
    private static final String AGGREGATE_TYPE = "SCREENING";

    /**
     * 상영 ID
     */
    private Long screeningId;

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
     * 업데이트 시간
     */
    private LocalDateTime updatedAt;

    /**
     * 상영 업데이트 이벤트 생성자
     * @param screening 상영 엔티티
     */
    public ScreeningUpdatedEvent(Screening screening) {
        super(EVENT_TYPE);
        this.screeningId = screening.getId();
        this.movieId = screening.getMovie().getId();
        this.movieTitle = screening.getMovie().getTitle();
        this.screenName = screening.getScreenName();
        this.startTime = screening.getStartTime();
        this.endTime = screening.getEndTime();
        this.totalSeats = screening.getTotalSeats();
        this.availableSeats = screening.getAvailableSeats();
        this.price = screening.getPrice();
        this.updatedAt = LocalDateTime.now();
        
        // 이벤트 키 생성
        setEventKey(generateEventKey(AGGREGATE_TYPE, screening.getId().toString()));
    }
}
