package com.study.ticket.domain.order.controller;

import com.study.ticket.domain.order.dto.*;
import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.order.domain.enums.OrderStatus;
import com.study.ticket.domain.order.service.OrderService;
import com.study.ticket.global.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 주문 컨트롤러
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    // 성공 응답 코드
    private static final String ORDER_CREATE_SUCCESS = "ORDER_CREATE_SUCCESS";
    private static final String ORDER_GET_SUCCESS = "ORDER_GET_SUCCESS";
    private static final String ORDER_LIST_SUCCESS = "ORDER_LIST_SUCCESS";
    private static final String ORDER_CANCEL_SUCCESS = "ORDER_CANCEL_SUCCESS";
    
    // 실패 응답 코드
    private static final String ORDER_CREATE_FAILED = "ORDER_CREATE_FAILED";
    private static final String ORDER_GET_FAILED = "ORDER_GET_FAILED";
    private static final String ORDER_LIST_FAILED = "ORDER_LIST_FAILED";
    private static final String ORDER_CANCEL_FAILED = "ORDER_CANCEL_FAILED";

    private final OrderService orderService;

    /**
     * 주문을 생성합니다.
     * @param orderSaveDto 주문 생성 요청
     * @return 생성된 주문
     */
    @PostMapping
    public ResponseEntity<ResponseDto<OrderResponse>> createOrder(@RequestBody OrderSaveDto orderSaveDto) {
        try {
            log.info("주문 생성 요청: {}", orderSaveDto);
            
            Order order = orderService.createOrder(orderSaveDto);
            OrderResponse response = OrderResponse.from(order);
            
            ResponseDto<OrderResponse> responseDto = ResponseDto.<OrderResponse>builder()
                .responseCode(ORDER_CREATE_SUCCESS)
                .responseMessage("주문이 성공적으로 생성되었습니다.")
                .data(response)
                .build();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            log.error("주문 생성 실패: {}", e.getMessage(), e);
            
            ResponseDto<OrderResponse> errorResponse = ResponseDto.<OrderResponse>builder()
                .responseCode(ORDER_CREATE_FAILED)
                .responseMessage("주문 생성에 실패했습니다: " + e.getMessage())
                .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 주문을 조회합니다.
     * @param orderId 주문 ID
     * @return 주문
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseDto<OrderDetailDto>> findOrderDetail(@PathVariable Long orderId) {
        try {
            log.info("주문 조회 요청: {}", orderId);
            
            OrderDetailDto detail = orderService.findOrderDetail(orderId);

            ResponseDto<OrderDetailDto> responseDto = ResponseDto.<OrderDetailDto>builder()
                .responseCode(ORDER_GET_SUCCESS)
                .responseMessage("주문 조회에 성공했습니다.")
                .data(detail)
                .build();
            
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("주문 조회 실패: {}", e.getMessage(), e);
            
            ResponseDto<OrderDetailDto> errorResponse = ResponseDto.<OrderDetailDto>builder()
                .responseCode(ORDER_GET_FAILED)
                .responseMessage("주문 조회에 실패했습니다: " + e.getMessage())
                .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
    * 주문 번호로 주문을 조회합니다.
    * @param orderNumber 주문 번호
    * @return 주문
    */
   @GetMapping("/number/{orderNumber}")
   public ResponseEntity<ResponseDto<OrderDetailDto>> findOrderDetailByOrderNumber(@PathVariable String orderNumber) {
       try {
           log.info("주문 번호로 조회 요청: {}", orderNumber);

           OrderDetailDto detail = orderService.findOrderDetailByOrderNumber(orderNumber);

           ResponseDto<OrderDetailDto> responseDto = ResponseDto.<OrderDetailDto>builder()
               .responseCode(ORDER_GET_SUCCESS)
               .responseMessage("주문 조회에 성공했습니다.")
               .data(detail)
               .build();

           return ResponseEntity.ok(responseDto);
       } catch (Exception e) {
           log.error("주문 번호로 조회 실패: {}", e.getMessage(), e);

           ResponseDto<OrderDetailDto> errorResponse = ResponseDto.<OrderDetailDto>builder()
               .responseCode(ORDER_GET_FAILED)
               .responseMessage("주문 조회에 실패했습니다: " + e.getMessage())
               .build();

           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
       }
   }

    /**
     * 사용자의 주문 목록을 조회합니다.
     * @param userId 사용자 ID
     * @param OrderCondDto orderCondDto
     * @param pageable page 정보
     * @return 주문 목록
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDto<Page<OrderListDto>>> getUserOrders(
            @PathVariable Long userId,
            @ModelAttribute OrderCondDto orderCondDto,
            @PageableDefault Pageable pageable
    ) {
        try {

            Page<OrderListDto> page = orderService.findOrderListWithPaging(userId, orderCondDto, pageable);

            ResponseDto<Page<OrderListDto>> responseDto = ResponseDto.<Page<OrderListDto>>builder()
                .responseCode(ORDER_LIST_SUCCESS)
                .responseMessage("주문 목록 조회에 성공했습니다.")
                .data(page)
                .build();
            
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("주문 목록 조회 실패: {}", e.getMessage(), e);
            
            ResponseDto<Page<OrderListDto>> errorResponse = ResponseDto.<Page<OrderListDto>>builder()
                .responseCode(ORDER_LIST_FAILED)
                .responseMessage("주문 목록 조회에 실패했습니다: " + e.getMessage())
                .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 주문을 취소합니다.
     * @param orderId 주문 ID
     * @return 취소된 주문
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ResponseDto<OrderResponse>> cancelOrder(@PathVariable Long orderId) {
        try {
            log.info("주문 취소 요청: {}", orderId);

            Order order = orderService.cancelOrder(orderId);
            OrderResponse response = OrderResponse.from(order);

            ResponseDto<OrderResponse> responseDto = ResponseDto.<OrderResponse>builder()
                .responseCode(ORDER_CANCEL_SUCCESS)
                .responseMessage("주문 취소에 성공했습니다.")
                .data(response)
                .build();

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("주문 취소 실패: {}", e.getMessage(), e);

            ResponseDto<OrderResponse> errorResponse = ResponseDto.<OrderResponse>builder()
                .responseCode(ORDER_CANCEL_FAILED)
                .responseMessage("주문 취소에 실패했습니다: " + e.getMessage())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    

}
