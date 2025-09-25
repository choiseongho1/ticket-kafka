package com.study.ticket.domain.ticket.controller;

import com.study.ticket.domain.ticket.dto.TicketCondDto;
import com.study.ticket.domain.ticket.dto.TicketDetailDto;
import com.study.ticket.domain.ticket.dto.TicketListDto;
import com.study.ticket.domain.ticket.dto.TicketResponse;
import com.study.ticket.domain.ticket.domain.entity.MovieTicket;
import com.study.ticket.domain.ticket.domain.enums.TicketStatus;
import com.study.ticket.domain.ticket.service.TicketService;
import com.study.ticket.global.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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


    // 성공 응답 코드
    private static final String TICKET_GET_SUCCESS = "TICKET_GET_SUCCESS";
    private static final String TICKET_LIST_SUCCESS = "TICKET_LIST_SUCCESS";
    private static final String TICKET_USE_SUCCESS = "TICKET_USE_SUCCESS";
    private static final String TICKET_CANCEL_SUCCESS = "TICKET_CANCEL_SUCCESS";

    // 실패 응답 코드
    private static final String TICKET_GET_FAILED = "TICKET_GET_FAILED";
    private static final String TICKET_LIST_FAILED = "TICKET_LIST_FAILED";
    private static final String TICKET_USE_FAILED = "TICKET_USE_FAILED";
    private static final String TICKET_CANCEL_FAILED = "TICKET_CANCEL_FAILED";

    private final TicketService ticketService;

    /**
     * 티켓을 조회합니다.
     * @param ticketId 티켓 ID
     * @return 티켓
     */
    @GetMapping("/{ticketId}")
    public ResponseEntity<ResponseDto<TicketDetailDto>> findTicketDetailByTicketId(@PathVariable Long ticketId) {
        try {
            log.info("티켓 조회 요청: {}", ticketId);

            TicketDetailDto detail = ticketService.findTicketDetailByTicketId(ticketId);

            ResponseDto<TicketDetailDto> responseDto = ResponseDto.<TicketDetailDto>builder()
                .responseCode(TICKET_GET_SUCCESS)
                .responseMessage("티켓 조회에 성공했습니다.")
                .data(detail)
                .build();

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("티켓 조회 실패: {}", e.getMessage(), e);

            ResponseDto<TicketDetailDto> errorResponse = ResponseDto.<TicketDetailDto>builder()
                .responseCode(TICKET_GET_FAILED)
                .responseMessage("티켓 조회에 실패했습니다: " + e.getMessage())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 티켓 번호로 티켓을 조회합니다.
     * @param ticketNumber 티켓 번호
     * @return 티켓
     */
    @GetMapping("/number/{ticketNumber}")
    public ResponseEntity<ResponseDto<TicketDetailDto>> findTicketDetailByTicketNumber(@PathVariable String ticketNumber) {
        try {
            log.info("티켓 조회 요청: {}", ticketNumber);

            TicketDetailDto detail = ticketService.findTicketDetailByTicketNumber(ticketNumber);

            ResponseDto<TicketDetailDto> responseDto = ResponseDto.<TicketDetailDto>builder()
                .responseCode(TICKET_GET_SUCCESS)
                .responseMessage("티켓 조회에 성공했습니다.")
                .data(detail)
                .build();

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("티켓 조회 실패: {}", e.getMessage(), e);

            ResponseDto<TicketDetailDto> errorResponse = ResponseDto.<TicketDetailDto>builder()
                .responseCode(TICKET_GET_FAILED)
                .responseMessage("티켓 조회에 실패했습니다: " + e.getMessage())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 주문에 대한 티켓 목록을 조회합니다.
     * @param orderId 주문 ID
     * @return 티켓 목록
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseDto<List<TicketListDto>>> findTicketListByOrderId(@PathVariable Long orderId) {
        try {
            log.info("주문별 티켓 목록 조회 요청: {}", orderId);

            List<TicketListDto> tickets = ticketService.findTicketListByOrderId(orderId);

            ResponseDto<List<TicketListDto>> responseDto = ResponseDto.<List<TicketListDto>>builder()
                .responseCode(TICKET_LIST_SUCCESS)
                .responseMessage("주문별 티켓 목록 조회에 성공했습니다.")
                .data(tickets)
                .build();

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("주문별 티켓 목록 조회 실패: {}", e.getMessage(), e);

            ResponseDto<List<TicketListDto>> errorResponse = ResponseDto.<List<TicketListDto>>builder()
                .responseCode(TICKET_LIST_FAILED)
                .responseMessage("주문별 티켓 목록 조회에 실패했습니다: " + e.getMessage())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    /**
     * 사용자의 티켓 목록을 조회합니다.
     * @param userId 사용자 ID
     * @param pageable 페이지 정보
     * @return 티켓 페이지
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDto<Page<TicketListDto>>> findTicketListWithPaging(
            @PathVariable Long userId,
            @ModelAttribute TicketCondDto ticketCondDto,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        try {
            log.info("사용자별 티켓 목록 조회 요청: {}", userId);

            Page<TicketListDto> tickets = ticketService.findTicketListWithPaging(userId, ticketCondDto, pageable);

            ResponseDto<Page<TicketListDto>> responseDto = ResponseDto.<Page<TicketListDto>>builder()
                .responseCode(TICKET_LIST_SUCCESS)
                .responseMessage("사용자별 티켓 목록 조회에 성공했습니다.")
                .data(tickets)
                .build();

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("사용자별 티켓 목록 조회 실패: {}", e.getMessage(), e);

            ResponseDto<Page<TicketListDto>> errorResponse = ResponseDto.<Page<TicketListDto>>builder()
                .responseCode(TICKET_LIST_FAILED)
                .responseMessage("사용자별 티켓 목록 조회에 실패했습니다: " + e.getMessage())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 티켓을 사용합니다.
     * @param ticketId 티켓 ID
     * @return 사용된 티켓
     */
    @PostMapping("/{ticketId}/use")
    public ResponseEntity<ResponseDto<?>> useTicket(@PathVariable Long ticketId) {
        try {
            log.info("티켓 사용 요청: {}", ticketId);

            MovieTicket ticket = ticketService.useTicket(ticketId);
            TicketResponse response = TicketResponse.from(ticket);

            ResponseDto<?> responseDto = ResponseDto.builder()
                .responseCode(TICKET_USE_SUCCESS)
                .responseMessage("티켓 사용에 성공했습니다.")
                .data(response)
                .build();

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("티켓 사용 실패: {}", e.getMessage(), e);

            ResponseDto<?> errorResponse = ResponseDto.builder()
                .responseCode(TICKET_USE_FAILED)
                .responseMessage("티켓 사용에 실패했습니다: " + e.getMessage())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 티켓을 취소합니다.
     * @param ticketId 티켓 ID
     * @return 취소된 티켓
     */
    @PostMapping("/{ticketId}/cancel")
    public ResponseEntity<ResponseDto<?>> cancelTicket(@PathVariable Long ticketId) {
        try {
            log.info("티켓 취소 요청: {}", ticketId);

            MovieTicket ticket = ticketService.cancelTicket(ticketId);
            TicketResponse response = TicketResponse.from(ticket);

            ResponseDto<?> responseDto = ResponseDto.builder()
                .responseCode(TICKET_CANCEL_SUCCESS)
                .responseMessage("티켓 취소에 성공했습니다.")
                .data(response)
                .build();

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("티켓 취소 실패: {}", e.getMessage(), e);

            ResponseDto<?> errorResponse = ResponseDto.builder()
                .responseCode(TICKET_CANCEL_FAILED)
                .responseMessage("티켓 취소에 실패했습니다: " + e.getMessage())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
