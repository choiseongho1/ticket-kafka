package com.study.ticket.domain.payment.domain.entity;

import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.payment.domain.enums.PaymentMethod;
import com.study.ticket.domain.payment.domain.enums.PaymentStatus;
import com.study.ticket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 결제 엔티티
 */
@Entity
@Table(name = "PAYMENTS")
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    @Comment("결제 ID")
    private Long id;

    @Column(name = "PAYMENT_NUMBER", nullable = false, unique = true)
    @Comment("결제 번호")
    private String paymentNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    @Comment("주문 ID (FK)")
    private Order order;

    @Column(name = "AMOUNT", nullable = false)
    @Comment("결제 금액")
    private Integer amount;

    @Column(name = "METHOD", nullable = false)
    @Comment("결제 방법")
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Column(name = "STATUS", nullable = false)
    @Comment("결제 상태")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "PAYMENT_KEY")
    @Comment("외부 결제 시스템 키")
    private String paymentKey;

    @Column(name = "PAYMENT_TIME")
    @Comment("결제 시간")
    private LocalDateTime paymentTime;

    @Column(name = "CANCEL_TIME")
    @Comment("취소 시간")
    private LocalDateTime cancelTime;

    @Column(name = "CANCEL_REASON")
    @Comment("취소 사유")
    private String cancelReason;

    @Column(name = "FAILURE_REASON")
    @Comment("실패 이유")
    private String failureReason;

    @Version
    @Column(name = "VERSION")
    @Comment("버전 (낙관적 락)")
    private Long version;

    /**
     * 결제를 생성합니다.
     */
    @PrePersist
    public void prePersist() {
        if (paymentNumber == null) {
            paymentNumber = UUID.randomUUID().toString();
        }
        if (status == null) {
            status = PaymentStatus.PENDING;
        }
    }

    /**
     * 결제를 승인합니다.
     * @param paymentKey 외부 결제 시스템 키
     */
    public void approve(String paymentKey) {
        this.paymentKey = paymentKey;
        this.status = PaymentStatus.COMPLETED;
        this.paymentTime = LocalDateTime.now();
    }

    /**
     * 결제를 실패 처리합니다.
     * @param reason 실패 이유
     */
    public void fail(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
    }

    /**
     * 결제를 취소합니다.
     * @param reason 취소 사유
     */
    public void cancel(String reason) {
        this.status = PaymentStatus.CANCELLED;
        this.cancelTime = LocalDateTime.now();
        this.cancelReason = reason;
    }

    /**
     * 결제가 완료되었는지 확인합니다.
     * @return 결제 완료 여부
     */
    public boolean isCompleted() {
        return this.status == PaymentStatus.COMPLETED;
    }
}
