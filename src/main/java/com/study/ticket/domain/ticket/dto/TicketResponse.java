package com.study.ticket.domain.ticket.dto;

import com.study.ticket.domain.ticket.domain.entity.MovieTicket;
import com.study.ticket.domain.ticket.domain.enums.TicketStatus;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class TicketResponse {

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

    /**
     * 티켓 엔티티를 DTO로 변환합니다.
     * @param ticket 티켓 엔티티
     * @return 티켓 응답 DTO
     */
    public static TicketResponse from(MovieTicket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber())
                .orderId(ticket.getOrder().getId())
                .userId(ticket.getUser().getId())
                .screeningId(ticket.getScreening().getId())
                .movieTitle(ticket.getScreening().getMovie().getTitle())
                .screenName(ticket.getScreening().getScreenName())
                .startTime(ticket.getScreening().getStartTime())
                .endTime(ticket.getScreening().getEndTime())
                .seatNumber(ticket.getSeatNumber())
                .status(ticket.getStatus())
                .issueTime(ticket.getIssueTime())
                .usedTime(ticket.getUsedTime())
                .qrCode(ticket.getQrCode())
                .build();
    }
}
