package com.study.ticket.domain.order.event;

import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.order.domain.entity.OrderItem;
import com.study.ticket.global.event.BaseEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문 생성 이벤트
 */
@Getter
@NoArgsConstructor
public class OrderCreatedEvent extends BaseEvent {

    private static final String EVENT_TYPE = "ORDER_CREATED";
    private static final String AGGREGATE_TYPE = "ORDER";

    /**
     * 주문 ID
     */
    private Long orderId;

    /**
     * 주문 번호
     */
    private String orderNumber;

    /**
     * 사용자 ID
     */
    private Long userId;

    /**
     * 상영 ID
     */
    private Long screeningId;

    /**
     * 좌석 수
     */
    private Integer seatCount;

    /**
     * 총 금액
     */
    private Integer totalAmount;

    /**
     * 좌석 번호 목록
     */
    private List<String> seatNumbers;

    /**
     * 주문 생성 이벤트 생성자
     * @param order 주문 엔티티
     */
    public OrderCreatedEvent(Order order) {
        super(EVENT_TYPE);
        this.orderId = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.userId = order.getUser().getId();
        this.screeningId = order.getScreening().getId();
        this.seatCount = order.getSeatCount();
        this.totalAmount = order.getTotalAmount();
        this.seatNumbers = order.getOrderItems().stream()
                .map(OrderItem::getSeatNumber)
                .collect(Collectors.toList());
        
        // 이벤트 키 생성
        setEventKey(generateEventKey(AGGREGATE_TYPE, order.getId().toString()));
    }
}
