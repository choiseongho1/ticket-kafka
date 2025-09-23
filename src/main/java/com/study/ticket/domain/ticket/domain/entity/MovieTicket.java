package com.study.ticket.domain.ticket.domain.entity;

import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.screening.domain.entity.Screening;
import com.study.ticket.domain.ticket.domain.enums.TicketStatus;
import com.study.ticket.domain.user.domain.entity.User;
import com.study.ticket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 영화 티켓 엔티티
 */
@Entity
@Table(name = "MOVIE_TICKETS")
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class MovieTicket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_ID")
    @Comment("티켓 ID")
    private Long id;

    @Column(name = "TICKET_NUMBER", nullable = false, unique = true)
    @Comment("티켓 번호")
    private String ticketNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    @Comment("주문 ID (FK)")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @Comment("사용자 ID (FK)")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREENING_ID", nullable = false)
    @Comment("상영 ID (FK)")
    private Screening screening;

    @Column(name = "SEAT_NUMBER", nullable = false)
    @Comment("좌석 번호")
    private String seatNumber;

    @Column(name = "STATUS", nullable = false)
    @Comment("티켓 상태")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Column(name = "ISSUE_TIME")
    @Comment("발급 시간")
    private LocalDateTime issueTime;

    @Column(name = "USED_TIME")
    @Comment("사용 시간")
    private LocalDateTime usedTime;

    @Column(name = "CANCELLATION_TIME")
    @Comment("취소 시간")
    private LocalDateTime cancellationTime;

    @Column(name = "QR_CODE")
    @Comment("QR 코드")
    private String qrCode;

    /**
     * 티켓을 생성합니다.
     */
    @PrePersist
    public void prePersist() {
        if (ticketNumber == null) {
            ticketNumber = UUID.randomUUID().toString();
        }
        if (status == null) {
            status = TicketStatus.ISSUED;
        }
        if (issueTime == null) {
            issueTime = LocalDateTime.now();
        }
        if (qrCode == null) {
            qrCode = generateQrCode();
        }
    }

    /**
     * QR 코드를 생성합니다.
     * @return 생성된 QR 코드
     */
    private String generateQrCode() {
        return UUID.randomUUID().toString();
    }

    /**
     * 티켓을 사용 처리합니다.
     */
    public void use() {
        this.status = TicketStatus.USED;
        this.usedTime = LocalDateTime.now();
    }

    /**
     * 티켓을 취소 처리합니다.
     */
    public void cancel() {
        this.status = TicketStatus.CANCELLED;
        this.cancellationTime = LocalDateTime.now();
    }

    /**
     * 티켓이 유효한지 확인합니다.
     * @return 티켓 유효 여부
     */
    public boolean isValid() {
        return this.status == TicketStatus.ISSUED;
    }
}
