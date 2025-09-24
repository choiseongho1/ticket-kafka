package com.study.ticket.domain.order.dto;

import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.order.domain.enums.OrderStatus;
import com.study.ticket.domain.screening.domain.entity.Screening;
import com.study.ticket.domain.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 주문 생성 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSaveDto {

    /**
     * 사용자 ID
     */
    private Long userId;

    /**
     * 상영 ID
     */
    private Long screeningId;

    /**
     * 좌석 번호 목록
     */
    private List<String> seatNumbers;


    public Order toEntity(User user, Screening screening) {
        return Order.builder()
                .user(user)
                .screening(screening)
                .seatCount(seatNumbers.size())
                .totalAmount(screening.getPrice() * seatNumbers.size())
                .status(OrderStatus.CREATED)
                .build();
    }
}
