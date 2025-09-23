package com.study.ticket.domain.screening.domain.entity;

import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 상영 엔티티
 */
@Entity
@Table(name = "SCREENINGS")
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Screening extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCREENING_ID")
    @Comment("상영 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID", nullable = false)
    @Comment("영화 ID (FK)")
    private Movie movie;

    @Column(name = "SCREEN_NAME", nullable = false)
    @Comment("상영관 이름")
    private String screenName;

    @Column(name = "START_TIME", nullable = false)
    @Comment("상영 시작 시간")
    private LocalDateTime startTime;

    @Column(name = "END_TIME", nullable = false)
    @Comment("상영 종료 시간")
    private LocalDateTime endTime;

    @Column(name = "TOTAL_SEATS", nullable = false)
    @Comment("총 좌석 수")
    private Integer totalSeats;

    @Column(name = "AVAILABLE_SEATS", nullable = false)
    @Comment("예약 가능한 좌석 수")
    private Integer availableSeats;

    @Column(name = "PRICE", nullable = false)
    @Comment("기본 티켓 가격")
    private Integer price;

    /**
     * 좌석 예약 가능 여부를 확인합니다.
     * @param count 예약할 좌석 수
     * @return 예약 가능 여부
     */
    public boolean isAvailable(int count) {
        return availableSeats >= count;
    }

    /**
     * 좌석을 예약합니다.
     * @param count 예약할 좌석 수
     * @return 예약 성공 여부
     */
    public boolean reserve(int count) {
        if (isAvailable(count)) {
            availableSeats -= count;
            return true;
        }
        return false;
    }

    /**
     * 좌석 예약을 취소합니다.
     * @param count 취소할 좌석 수
     */
    public void cancelReservation(int count) {
        availableSeats += count;
        if (availableSeats > totalSeats) {
            availableSeats = totalSeats;
        }
    }
}
