package com.study.ticket.domain.payment.domain.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.ticket.domain.order.dto.*;
import com.study.ticket.domain.payment.dto.PaymentDetailDto;
import com.study.ticket.domain.payment.dto.QPaymentDetailDto;
import com.study.ticket.global.util.QueryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.study.ticket.domain.order.domain.entity.QOrder.order;
import static com.study.ticket.domain.order.domain.entity.QOrderItem.orderItem;
import static com.study.ticket.domain.payment.domain.entity.QPayment.payment;

/**
 * MemoDslRepository 구현체
 */
@Repository
@RequiredArgsConstructor
public class PaymentDslRepositoryImpl implements PaymentDslRepository {

    private final JPAQueryFactory queryFactory;



    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public Optional<PaymentDetailDto> findPaymentDetailByPaymentId(Long id){
        // 영화 기본 정보 조회
        PaymentDetailDto paymentDetailDto = queryFactory
                .select(new QPaymentDetailDto(
                        payment.id,
                        payment.paymentNumber,
                        payment.order.id,
                        payment.order.orderNumber,
                        payment.amount,
                        payment.method,
                        payment.status,
                        payment.paymentKey,
                        payment.paymentTime,
                        payment.cancelTime,
                        payment.cancelReason,
                        payment.createdAt
                ))
                .from(payment)
                .where(payment.id.eq(id))
                .fetchOne();

        // 존재하지 않으면 빈 Optional 반환
        if (paymentDetailDto == null) {
            return Optional.empty();
        }


        return Optional.of(paymentDetailDto);
    }


    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public Optional<PaymentDetailDto> findPaymentDetailByOrderId(Long orderId){
        // 영화 기본 정보 조회
        PaymentDetailDto paymentDetailDto = queryFactory
                .select(new QPaymentDetailDto(
                        payment.id,
                        payment.paymentNumber,
                        payment.order.id,
                        payment.order.orderNumber,
                        payment.amount,
                        payment.method,
                        payment.status,
                        payment.paymentKey,
                        payment.paymentTime,
                        payment.cancelTime,
                        payment.cancelReason,
                        payment.createdAt
                ))
                .from(payment)
                .where(payment.order.id.eq(orderId))
                .fetchOne();

        // 존재하지 않으면 빈 Optional 반환
        if (paymentDetailDto == null) {
            return Optional.empty();
        }


        return Optional.of(paymentDetailDto);
    }
}
