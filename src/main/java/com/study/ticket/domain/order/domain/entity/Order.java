package com.study.ticket.domain.order.domain.entity;

import com.study.ticket.domain.order.domain.enums.OrderStatus;
import com.study.ticket.domain.screening.domain.entity.Screening;
import com.study.ticket.domain.user.domain.entity.User;
import com.study.ticket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 주문 엔티티
 */
@Entity
@Table(name = "ORDERS")
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    @Comment("주문 ID")
    private Long id;

    @Column(name = "ORDER_NUMBER", nullable = false, unique = true)
    @Comment("주문 번호")
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @Comment("사용자 ID (FK)")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREENING_ID", nullable = false)
    @Comment("상영 ID (FK)")
    private Screening screening;

    @Column(name = "SEAT_COUNT", nullable = false)
    @Comment("예약 좌석 수")
    private Integer seatCount;

    @Column(name = "TOTAL_AMOUNT", nullable = false)
    @Comment("총 주문 금액")
    private Integer totalAmount;

    @Column(name = "STATUS", nullable = false)
    @Comment("주문 상태")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "PAYMENT_DEADLINE")
    @Comment("결제 마감 시간")
    private LocalDateTime paymentDeadline;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @Comment("주문 항목 목록")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Version
    @Column(name = "VERSION")
    @Comment("버전 (낙관적 락)")
    private Long version;

    /**
     * 주문을 생성합니다.
     */
    @PrePersist
    public void prePersist() {
        if (orderNumber == null) {
            orderNumber = UUID.randomUUID().toString();
        }
        if (status == null) {
            status = OrderStatus.CREATED;
        }
        if (paymentDeadline == null) {
            // 결제 마감 시간은 주문 생성 후 30분으로 설정
            paymentDeadline = LocalDateTime.now().plusMinutes(30);
        }
    }

    /**
     * 주문 항목을 추가합니다.
     * @param orderItem 주문 항목
     */
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 주문 상태를 결제 대기 상태로 변경합니다.
     */
    public void waitForPayment() {
        this.status = OrderStatus.PAYMENT_PENDING;
    }

    /**
     * 주문 상태를 결제 완료 상태로 변경합니다.
     */
    public void completePayment() {
        this.status = OrderStatus.PAYMENT_COMPLETED;
    }

    /**
     * 주문 상태를 취소 상태로 변경합니다.
     */
    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    /**
     * 주문 상태를 완료 상태로 변경합니다.
     */
    public void complete() {
        this.status = OrderStatus.COMPLETED;
    }

    /**
     * 주문 상태를 실패 상태로 변경합니다.
     */
    public void fail() {
        this.status = OrderStatus.FAILED;
    }

    /**
     * 결제 마감 시간이 지났는지 확인합니다.
     * @return 결제 마감 시간 경과 여부
     */
    public boolean isPaymentDeadlineExpired() {
        return LocalDateTime.now().isAfter(paymentDeadline);
    }
}
