package com.study.ticket.domain.order.domain.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.ticket.domain.movie.dto.*;
import com.study.ticket.domain.order.dto.*;
import com.study.ticket.global.util.QueryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.study.ticket.domain.movie.domain.entity.QMovie.movie;
import static com.study.ticket.domain.order.domain.entity.QOrder.order;
import static com.study.ticket.domain.order.domain.entity.QOrderItem.orderItem;

/**
 * MemoDslRepository 구현체
 */
@Repository
@RequiredArgsConstructor
public class OrderDslRepositoryImpl implements OrderDslRepository {

    private final JPAQueryFactory queryFactory;


    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public Page<OrderListDto> findOrderListWithPaging(Long userId, OrderCondDto orderCondDto, Pageable pageable) {
        // 전체 카운트 조회
        final Long totalCount = findOrderListCount(userId, orderCondDto).fetchOne();

        // 데이터 조회
        final List<OrderListDto> content = findOrderList(userId, orderCondDto)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(content, pageable, (totalCount==null)? 0 : totalCount);

    }

    // findOrderListWithPaging 전체 카운트 조회
    private JPAQuery<Long> findOrderListCount(Long userId, OrderCondDto orderCondDto){
        return queryFactory
                .select(order.count())
                .from(order)
                .where(orderListSearchCondition(userId, orderCondDto));
    }

    // findOrderListWithPaging 목록 조회
    private JPAQuery<OrderListDto> findOrderList(Long userId, OrderCondDto orderCondDto){
        return queryFactory
                .select(new QOrderListDto(
                        order.id,
                        order.orderNumber,
                        order.user.id,
                        order.screening.id,
                        order.screening.movie.title,
                        order.screening.screenName,
                        order.screening.startTime,
                        order.seatCount,
                        order.totalAmount,
                        order.status,
                        order.paymentDeadline,
                        order.createdAt
                ))
                .from(order)
                .where(orderListSearchCondition(userId, orderCondDto));
    }

    // findOrderListWithPaging 조회 조건
    private Predicate[] orderListSearchCondition(Long userId, OrderCondDto orderCondDto){
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(order.user.id.eq(userId));

        predicates.add(QueryUtils.eq(orderCondDto.getStatus(), order.status));

        return predicates.toArray(new Predicate[0]);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public Optional<OrderDetailDto> findOrderDetailByOrderId(Long id){
        // 영화 기본 정보 조회
        OrderDetailDto orderDetailDto = queryFactory
                .select(new QOrderDetailDto(
                        order.id,
                        order.orderNumber,
                        order.user.id,
                        order.screening.id,
                        order.screening.movie.title,
                        order.screening.screenName,
                        order.screening.startTime,
                        order.seatCount,
                        order.totalAmount,
                        order.status,
                        order.paymentDeadline,
                        order.createdAt
                ))
                .from(order)
                .where(order.id.eq(id))
                .fetchOne();

        // 존재하지 않으면 빈 Optional 반환
        if (orderDetailDto == null) {
            return Optional.empty();
        }

        // 주문 항목(좌석 번호) 조회
        List<String> seatNumbers = queryFactory
                .select(orderItem.seatNumber)
                .from(orderItem)
                .where(orderItem.order.id.eq(id))
                .fetch();

        // 좌석 번호 설정
        orderDetailDto.setSeatNumbers(seatNumbers);

        return Optional.of(orderDetailDto);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public Optional<OrderDetailDto> findOrderDetailByOrderNumber(String orderNumber){
        // 영화 기본 정보 조회
        OrderDetailDto orderDetailDto = queryFactory
                .select(new QOrderDetailDto(
                        order.id,
                        order.orderNumber,
                        order.user.id,
                        order.screening.id,
                        order.screening.movie.title,
                        order.screening.screenName,
                        order.screening.startTime,
                        order.seatCount,
                        order.totalAmount,
                        order.status,
                        order.paymentDeadline,
                        order.createdAt
                ))
                .from(order)
                .where(order.orderNumber.eq(orderNumber))
                .fetchOne();

        // 존재하지 않으면 빈 Optional 반환
        if (orderDetailDto == null) {
            return Optional.empty();
        }

        // 주문 항목(좌석 번호) 조회
        List<String> seatNumbers = queryFactory
                .select(orderItem.seatNumber)
                .from(orderItem)
                .where(orderItem.order.orderNumber.eq(orderNumber))
                .fetch();

        // 좌석 번호 설정
        orderDetailDto.setSeatNumbers(seatNumbers);

        return Optional.of(orderDetailDto);
    }
    

}
