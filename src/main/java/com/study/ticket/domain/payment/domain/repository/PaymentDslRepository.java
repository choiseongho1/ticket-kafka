package com.study.ticket.domain.payment.domain.repository;

import com.study.ticket.domain.order.dto.OrderCondDto;
import com.study.ticket.domain.order.dto.OrderDetailDto;
import com.study.ticket.domain.order.dto.OrderListDto;
import com.study.ticket.domain.payment.dto.PaymentDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PaymentDslRepository {

    Optional<PaymentDetailDto> findPaymentDetailByPaymentId(Long id);
    Optional<PaymentDetailDto> findPaymentDetailByOrderId(Long orderId);
}
