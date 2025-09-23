package com.study.ticket.domain.ticket.event;

import com.study.ticket.domain.ticket.domain.entity.MovieTicket;
import com.study.ticket.global.event.BaseEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 티켓 사용 이벤트
 */
@Getter
@NoArgsConstructor
public class TicketUsedEvent extends BaseEvent {

    private static final String EVENT_TYPE = "TICKET_USED";
    private static final String AGGREGATE_TYPE = "TICKET";

    /**
     * 티켓 ID
     */
    private Long ticketId;

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
     * 좌석 번호
     */
    private String seatNumber;

    /**
     * 사용 시간
     */
    private LocalDateTime usedTime;

    /**
     * 티켓 사용 이벤트 생성자
     * @param ticket 티켓 엔티티
     */
    public TicketUsedEvent(MovieTicket ticket) {
        super(EVENT_TYPE);
        this.ticketId = ticket.getId();
        this.ticketNumber = ticket.getTicketNumber();
        this.orderId = ticket.getOrder().getId();
        this.userId = ticket.getUser().getId();
        this.screeningId = ticket.getScreening().getId();
        this.seatNumber = ticket.getSeatNumber();
        this.usedTime = ticket.getUsedTime();
        
        // 이벤트 키 생성
        setEventKey(generateEventKey(AGGREGATE_TYPE, ticket.getId().toString()));
    }
}
