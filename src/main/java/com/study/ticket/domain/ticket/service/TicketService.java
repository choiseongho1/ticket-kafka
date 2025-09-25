package com.study.ticket.domain.ticket.service;

import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.order.domain.entity.OrderItem;
import com.study.ticket.domain.order.domain.repository.OrderRepository;
import com.study.ticket.domain.order.service.OrderService;
import com.study.ticket.domain.outbox.service.OutboxEventService;
import com.study.ticket.domain.ticket.domain.entity.MovieTicket;
import com.study.ticket.domain.ticket.domain.enums.TicketStatus;
import com.study.ticket.domain.ticket.domain.repository.MovieTicketRepository;
import com.study.ticket.domain.ticket.dto.TicketCondDto;
import com.study.ticket.domain.ticket.dto.TicketDetailDto;
import com.study.ticket.domain.ticket.dto.TicketListDto;
import com.study.ticket.domain.ticket.event.TicketIssuedEvent;
import com.study.ticket.domain.ticket.event.TicketUsedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 티켓 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final MovieTicketRepository movieTicketRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final OutboxEventService outboxEventService;
    
    @Value("${kafka.topics.ticket-events}")
    private String ticketEventsTopic;
    
    /**
     * 티켓을 발급합니다.
     * @param orderId 주문 ID
     */
    @Transactional
    public void issueTickets(Long orderId) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없음: " + orderId));
        
        // 이미 티켓이 발급되었는지 확인
        List<MovieTicket> existingTickets = movieTicketRepository.findByOrder(order);
        if (!existingTickets.isEmpty()) {
            log.warn("이미 티켓이 발급되었습니다: {}", orderId);
            return ;
        }
        
        List<MovieTicket> tickets = new ArrayList<>();
        
        // 주문 항목별로 티켓 발급
        for (OrderItem orderItem : order.getOrderItems()) {
            MovieTicket ticket = MovieTicket.builder()
                    .order(order)
                    .user(order.getUser())
                    .screening(order.getScreening())
                    .seatNumber(orderItem.getSeatNumber())
                    .status(TicketStatus.ISSUED)
                    .issueTime(LocalDateTime.now())
                    .build();
            
            tickets.add(movieTicketRepository.save(ticket));
            
            // 티켓 발급 이벤트 발행
            TicketIssuedEvent event = new TicketIssuedEvent(ticket);
            outboxEventService.saveEvent(
                    event,
                    "TICKET",
                    ticket.getId().toString(),
                    ticketEventsTopic
            );
        }
        
        // 주문 상태 업데이트
        orderService.completeOrder(orderId);
        
        log.info("티켓 발급 완료: 주문 ID={}, 티켓 수={}", orderId, tickets.size());
    }
    
    /**
     * 티켓을 조회합니다.
     * @param ticketId 티켓 ID
     * @return 티켓
     */
    @Transactional(readOnly = true)
    public TicketDetailDto findTicketDetailByTicketId(Long ticketId) {
        return movieTicketRepository.findTicketDetailByTicketId(ticketId)
                .orElseThrow(() -> new RuntimeException("티켓을 찾을 수 없음: " + ticketId));
    }
    
    /**
     * 티켓 번호로 티켓을 조회합니다.
     * @param ticketNumber 티켓 번호
     * @return 티켓
     */
    @Transactional(readOnly = true)
    public TicketDetailDto findTicketDetailByTicketNumber(String ticketNumber) {
        return movieTicketRepository.findTicketDetailByTicketNumber(ticketNumber)
                .orElseThrow(() -> new RuntimeException("티켓을 찾을 수 없음: " + ticketNumber));
    }
    
    /**
     * 주문에 대한 티켓 목록을 조회합니다.
     * @param orderId 주문 ID
     * @return 티켓 목록
     */
    @Transactional(readOnly = true)
    public List<TicketListDto> findTicketListByOrderId(Long orderId) {
        orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없음: " + orderId));
        return movieTicketRepository.findTicketListByOrderId(orderId);
    }
    
    /**
     * 사용자의 티켓 목록을 조회합니다.
     * @param userId 사용자 ID
     * @param TicketCondDto ticketCondDto
     * @param pageable 페이지 정보
     * @return 티켓 페이지
     */
    @Transactional(readOnly = true)
    public Page<TicketListDto> findTicketListWithPaging(Long userId, TicketCondDto ticketCondDto, Pageable pageable) {
        return movieTicketRepository.findTicketListWithPaging(userId,ticketCondDto , pageable);
    }
    

    /**
     * 티켓을 사용합니다.
     * @param ticketId 티켓 ID
     * @return 사용된 티켓
     */
    @Transactional
    public MovieTicket useTicket(Long ticketId) {
        MovieTicket ticket = movieTicketRepository.findById(ticketId)
                .orElseThrow(()-> new RuntimeException("올바르지 않은 티켓 정보 입니다." + ticketId));
        
        // 티켓 상태 확인
        if (ticket.getStatus() != TicketStatus.ISSUED) {
            throw new RuntimeException("사용 가능한 상태가 아닙니다: " + ticket.getStatus());
        }
        
        // 티켓 사용
        ticket.use();
        MovieTicket savedTicket = movieTicketRepository.save(ticket);
        
        // 티켓 사용 이벤트 발행
        TicketUsedEvent event = new TicketUsedEvent(savedTicket);
        outboxEventService.saveEvent(
                event,
                "TICKET",
                savedTicket.getId().toString(),
                ticketEventsTopic
        );
        
        log.info("티켓 사용 완료: {}", savedTicket.getTicketNumber());
        return savedTicket;
    }
    
    /**
     * 티켓을 취소합니다.
     * @param ticketId 티켓 ID
     * @return 취소된 티켓
     */
    @Transactional
    public MovieTicket cancelTicket(Long ticketId) {
        MovieTicket ticket = movieTicketRepository.findById(ticketId)
                .orElseThrow(()-> new RuntimeException("올바르지 않은 티켓 정보 입니다." + ticketId));
        
        // 티켓 상태 확인
        if (ticket.getStatus() != TicketStatus.ISSUED) {
            throw new RuntimeException("취소 가능한 상태가 아닙니다: " + ticket.getStatus());
        }
        
        // 티켓 취소
        ticket.cancel();
        return movieTicketRepository.save(ticket);
    }
    
    /**
     * 만료된 티켓을 처리합니다.
     * @return 처리된 티켓 수
     */
    @Transactional
    public int processExpiredTickets() {
        List<MovieTicket> expiredTickets = movieTicketRepository.findExpiredTickets(LocalDateTime.now());
        
        int count = 0;
        for (MovieTicket ticket : expiredTickets) {
            if (ticket.getStatus() == TicketStatus.ISSUED) {
                ticket.setStatus(TicketStatus.EXPIRED);
                movieTicketRepository.save(ticket);
                count++;
            }
        }
        
        log.info("만료된 티켓 처리 완료: {} 개", count);
        return count;
    }
}
