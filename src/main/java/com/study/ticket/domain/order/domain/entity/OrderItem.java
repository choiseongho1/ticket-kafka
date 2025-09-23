package com.study.ticket.domain.order.domain.entity;

import com.study.ticket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

/**
 * 주문 항목 엔티티
 */
@Entity
@Table(name = "ORDER_ITEMS")
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ITEM_ID")
    @Comment("주문 항목 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    @Comment("주문 ID (FK)")
    private Order order;

    @Column(name = "SEAT_NUMBER", nullable = false)
    @Comment("좌석 번호")
    private String seatNumber;

    @Column(name = "PRICE", nullable = false)
    @Comment("가격")
    private Integer price;
}
