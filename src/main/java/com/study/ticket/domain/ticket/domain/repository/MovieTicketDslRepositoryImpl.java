package com.study.ticket.domain.ticket.domain.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.ticket.domain.order.dto.OrderCondDto;
import com.study.ticket.domain.order.dto.OrderListDto;
import com.study.ticket.domain.order.dto.QOrderListDto;
import com.study.ticket.domain.payment.dto.PaymentDetailDto;
import com.study.ticket.domain.payment.dto.QPaymentDetailDto;
import com.study.ticket.domain.ticket.dto.*;
import com.study.ticket.global.util.QueryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.study.ticket.domain.payment.domain.entity.QPayment.payment;
import static com.study.ticket.domain.ticket.domain.entity.QMovieTicket.movieTicket;

/**
 * MemoDslRepository 구현체
 */
@Repository
@RequiredArgsConstructor
public class MovieTicketDslRepositoryImpl implements MovieTicketDslRepository {

    private final JPAQueryFactory queryFactory;


    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public Page<TicketListDto> findTicketListWithPaging(Long userId, TicketCondDto ticketCondDto, Pageable pageable) {
        // 전체 카운트 조회
        final Long totalCount = findTicketListCount(userId, ticketCondDto).fetchOne();

        // 데이터 조회
        final List<TicketListDto> content = findTicketList(userId, ticketCondDto)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(content, pageable, (totalCount==null)? 0 : totalCount);

    }

    // findTicketListWithPaging 전체 카운트 조회
    private JPAQuery<Long> findTicketListCount(Long userId, TicketCondDto ticketCondDto){
        return queryFactory
                .select(movieTicket.count())
                .from(movieTicket)
                .where(orderListSearchCondition(userId, ticketCondDto));
    }

    // findTicketListWithPaging 목록 조회
    private JPAQuery<TicketListDto> findTicketList(Long userId, TicketCondDto ticketCondDto){
        return queryFactory
                .select(new QTicketListDto(
                        movieTicket.id,
                        movieTicket.ticketNumber,
                        movieTicket.order.id,
                        movieTicket.user.id,
                        movieTicket.screening.id,
                        movieTicket.screening.movie.title,
                        movieTicket.screening.screenName,
                        movieTicket.screening.startTime,
                        movieTicket.screening.endTime,
                        movieTicket.seatNumber,
                        movieTicket.status,
                        movieTicket.issueTime,
                        movieTicket.usedTime,
                        movieTicket.qrCode
                ))
                .from(movieTicket)
                .where(orderListSearchCondition(userId, ticketCondDto));
    }

    // findTicketListWithPaging 조회 조건
    private Predicate[] orderListSearchCondition(Long userId, TicketCondDto ticketCondDto){
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(movieTicket.user.id.eq(userId));
        predicates.add(QueryUtils.eq(ticketCondDto.getStatus(), movieTicket.status));

        return predicates.toArray(new Predicate[0]);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public Optional<TicketDetailDto> findTicketDetailByTicketId(Long id){
        // 영화 기본 정보 조회
        TicketDetailDto ticketDetailDto = queryFactory
                .select(new QTicketDetailDto(
                        movieTicket.id,
                        movieTicket.ticketNumber,
                        movieTicket.order.id,
                        movieTicket.user.id,
                        movieTicket.screening.id,
                        movieTicket.screening.movie.title,
                        movieTicket.screening.screenName,
                        movieTicket.screening.startTime,
                        movieTicket.screening.endTime,
                        movieTicket.seatNumber,
                        movieTicket.status,
                        movieTicket.issueTime,
                        movieTicket.usedTime,
                        movieTicket.qrCode
                ))
                .from(movieTicket)
                .where(movieTicket.id.eq(id))
                .fetchOne();

        // 존재하지 않으면 빈 Optional 반환
        if (ticketDetailDto == null) {
            return Optional.empty();
        }


        return Optional.of(ticketDetailDto);
    }


    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public Optional<TicketDetailDto> findTicketDetailByTicketNumber(String ticketNumber){
        // 영화 기본 정보 조회
        TicketDetailDto ticketDetailDto = queryFactory
                .select(new QTicketDetailDto(
                        movieTicket.id,
                        movieTicket.ticketNumber,
                        movieTicket.order.id,
                        movieTicket.user.id,
                        movieTicket.screening.id,
                        movieTicket.screening.movie.title,
                        movieTicket.screening.screenName,
                        movieTicket.screening.startTime,
                        movieTicket.screening.endTime,
                        movieTicket.seatNumber,
                        movieTicket.status,
                        movieTicket.issueTime,
                        movieTicket.usedTime,
                        movieTicket.qrCode
                ))
                .from(movieTicket)
                .where(movieTicket.ticketNumber.eq(ticketNumber))
                .fetchOne();

        // 존재하지 않으면 빈 Optional 반환
        if (ticketDetailDto == null) {
            return Optional.empty();
        }

        return Optional.of(ticketDetailDto);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public List<TicketListDto> findTicketListByOrderId(Long orderId){
        return queryFactory
                .select(new QTicketListDto(
                        movieTicket.id,
                        movieTicket.ticketNumber,
                        movieTicket.order.id,
                        movieTicket.user.id,
                        movieTicket.screening.id,
                        movieTicket.screening.movie.title,
                        movieTicket.screening.screenName,
                        movieTicket.screening.startTime,
                        movieTicket.screening.endTime,
                        movieTicket.seatNumber,
                        movieTicket.status,
                        movieTicket.issueTime,
                        movieTicket.usedTime,
                        movieTicket.qrCode
                ))
                .from(movieTicket)
                .where(movieTicket.order.id.eq(orderId))
                .fetch();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------//

}
