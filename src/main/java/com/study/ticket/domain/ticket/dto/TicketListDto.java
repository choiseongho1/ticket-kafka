package com.study.ticket.domain.ticket.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.study.ticket.domain.ticket.domain.enums.TicketStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 티켓 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
public class TicketListDto {

    /**
     * 티켓 ID
     */
    private Long id;

    /**
     * 티켓 번호
     */
    private String ticketNumber;

    /**
     * 주문 ID
     */
    private Long orderId;

    /**
     * 사용자 ID
     */
    private Long userId;

    /**
     * 상영 ID
     */
    private Long screeningId;

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
     * 좌석 번호
     */
    private String seatNumber;

    /**
     * 티켓 상태
     */
    private TicketStatus status;

    /**
     * 발급 시간
     */
    private LocalDateTime issueTime;

    /**
     * 사용 시간
     */
    private LocalDateTime usedTime;

    /**
     * QR 코드
     */
    private String qrCode;



    @QueryProjection
    public TicketListDto(Long id, String ticketNumber, Long orderId, Long userId, Long screeningId, String movieTitle, String screenName, LocalDateTime startTime, LocalDateTime endTime, String seatNumber, TicketStatus status, LocalDateTime issueTime, LocalDateTime usedTime, String qrCode){
        this.id = id;
        this.ticketNumber = ticketNumber;
        this.orderId = orderId;
        this.userId = userId;
        this.screeningId = screeningId;
        this.movieTitle = movieTitle;
        this.screenName = screenName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seatNumber = seatNumber;
        this.status = status;
        this.issueTime = issueTime;
        this.usedTime = usedTime;
        this.qrCode = qrCode;

    }
}
