package com.study.ticket.domain.ticket.controller;

import com.study.ticket.domain.ticket.dto.TicketResponse;
import com.study.ticket.domain.ticket.domain.entity.MovieTicket;
import com.study.ticket.domain.ticket.domain.enums.TicketStatus;
import com.study.ticket.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 티켓 컨트롤러
 */
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    /**
     * 티켓을 조회합니다.
     * @param ticketId 티켓 ID
     * @return 티켓
     */
    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long ticketId) {
        log.info("티켓 조회 요청: {}", ticketId);
        
        MovieTicket ticket = ticketService.getTicket(ticketId);
        TicketResponse response = TicketResponse.from(ticket);
        return ResponseEntity.ok(response);
    }

    /**
     * 티켓 번호로 티켓을 조회합니다.
     * @param ticketNumber 티켓 번호
     * @return 티켓
     */
    @GetMapping("/number/{ticketNumber}")
    public ResponseEntity<TicketResponse> getTicketByNumber(@PathVariable String ticketNumber) {
        log.info("티켓 번호로 조회 요청: {}", ticketNumber);
        
        MovieTicket ticket = ticketService.getTicketByNumber(ticketNumber);
        TicketResponse response = TicketResponse.from(ticket);
        return ResponseEntity.ok(response);
    }

    /**
     * 주문에 대한 티켓 목록을 조회합니다.
     * @param orderId 주문 ID
     * @return 티켓 목록
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<TicketResponse>> getTicketsByOrder(@PathVariable Long orderId) {
        log.info("주문별 티켓 목록 조회 요청: {}", orderId);
        
        List<MovieTicket> tickets = ticketService.getTicketsByOrder(orderId);
        List<TicketResponse> responses = tickets.stream()
                .map(TicketResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * 사용자의 티켓 목록을 조회합니다.
     * @param userId 사용자 ID
     * @param pageable 페이지 정보
     * @return 티켓 페이지
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<TicketResponse>> getUserTickets(
            @PathVariable Long userId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        log.info("사용자별 티켓 목록 조회 요청: {}", userId);
        
        Page<MovieTicket> tickets = ticketService.getUserTickets(userId, pageable);
        Page<TicketResponse> responses = tickets.map(TicketResponse::from);
        return ResponseEntity.ok(responses);
    }

    /**
     * 사용자의 특정 상태의 티켓 목록을 조회합니다.
     * @param userId 사용자 ID
     * @param status 티켓 상태
     * @param pageable 페이지 정보
     * @return 티켓 페이지
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<Page<TicketResponse>> getUserTicketsByStatus(
            @PathVariable Long userId,
            @PathVariable TicketStatus status,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        log.info("사용자별 상태별 티켓 목록 조회 요청: {}, {}", userId, status);
        
        Page<MovieTicket> tickets = ticketService.getUserTicketsByStatus(userId, status, pageable);
        Page<TicketResponse> responses = tickets.map(TicketResponse::from);
        return ResponseEntity.ok(responses);
    }

    /**
     * 티켓을 사용합니다.
     * @param ticketId 티켓 ID
     * @return 사용된 티켓
     */
    @PostMapping("/{ticketId}/use")
    public ResponseEntity<TicketResponse> useTicket(@PathVariable Long ticketId) {
        log.info("티켓 사용 요청: {}", ticketId);
        
        MovieTicket ticket = ticketService.useTicket(ticketId);
        TicketResponse response = TicketResponse.from(ticket);
        return ResponseEntity.ok(response);
    }

    /**
     * 티켓을 취소합니다.
     * @param ticketId 티켓 ID
     * @return 취소된 티켓
     */
    @PostMapping("/{ticketId}/cancel")
    public ResponseEntity<TicketResponse> cancelTicket(@PathVariable Long ticketId) {
        log.info("티켓 취소 요청: {}", ticketId);
        
        MovieTicket ticket = ticketService.cancelTicket(ticketId);
        TicketResponse response = TicketResponse.from(ticket);
        return ResponseEntity.ok(response);
    }
}
