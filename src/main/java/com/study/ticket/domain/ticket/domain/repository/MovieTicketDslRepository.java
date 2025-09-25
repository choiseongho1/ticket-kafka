package com.study.ticket.domain.ticket.domain.repository;

import com.study.ticket.domain.order.dto.OrderCondDto;
import com.study.ticket.domain.order.dto.OrderListDto;
import com.study.ticket.domain.payment.dto.PaymentDetailDto;
import com.study.ticket.domain.ticket.dto.TicketCondDto;
import com.study.ticket.domain.ticket.dto.TicketDetailDto;
import com.study.ticket.domain.ticket.dto.TicketListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MovieTicketDslRepository {

    Page<TicketListDto> findTicketListWithPaging(Long userId, TicketCondDto ticketCondDto, Pageable pageable);
    Optional<TicketDetailDto> findTicketDetailByTicketId(Long id);
    Optional<TicketDetailDto> findTicketDetailByTicketNumber(String ticketNumber);
    List<TicketListDto> findTicketListByOrderId(Long orderId);

}
