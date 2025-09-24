package com.study.ticket.domain.order.domain.repository;

import com.study.ticket.domain.movie.dto.MovieCondDto;
import com.study.ticket.domain.movie.dto.MovieDetailDto;
import com.study.ticket.domain.movie.dto.MovieListDto;
import com.study.ticket.domain.order.dto.OrderCondDto;
import com.study.ticket.domain.order.dto.OrderDetailDto;
import com.study.ticket.domain.order.dto.OrderListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderDslRepository {

    Page<OrderListDto> findOrderListWithPaging(Long userId, OrderCondDto orderCondDto, Pageable pageable);

    Optional<OrderDetailDto> findOrderDetailByOrderId(Long id);

    Optional<OrderDetailDto> findOrderDetailByOrderNumber(String orderNumber);
}
