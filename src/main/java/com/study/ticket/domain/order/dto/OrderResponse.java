package com.study.ticket.domain.order.dto;

import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.order.domain.entity.OrderItem;
import com.study.ticket.domain.order.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    /**
     * 주문 ID
     */
    private Long id;

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
     * 좌석 수
     */
    private Integer seatCount;

    /**
     * 좌석 번호 목록
     */
    private List<String> seatNumbers;

    /**
     * 총 금액
     */
    private Integer totalAmount;

    /**
     * 주문 상태
     */
    private OrderStatus status;

    /**
     * 결제 마감 시간
     */
    private LocalDateTime paymentDeadline;

    /**
     * 생성 시간
     */
    private LocalDateTime createdAt;

    /**
     * 주문 엔티티를 DTO로 변환합니다.
     * @param order 주문 엔티티
     * @return 주문 응답 DTO
     */
    public static OrderResponse from(Order order) {
        List<String> seatNumbers = order.getOrderItems().stream()
                .map(OrderItem::getSeatNumber)
                .collect(Collectors.toList());
        
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUser().getId())
                .screeningId(order.getScreening().getId())
                .movieTitle(order.getScreening().getMovie().getTitle())
                .screenName(order.getScreening().getScreenName())
                .startTime(order.getScreening().getStartTime())
                .seatCount(order.getSeatCount())
                .seatNumbers(seatNumbers)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .paymentDeadline(order.getPaymentDeadline())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
