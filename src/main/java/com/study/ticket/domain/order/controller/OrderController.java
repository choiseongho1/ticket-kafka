package com.study.ticket.domain.order.controller;

import com.study.ticket.domain.order.controller.dto.OrderCreateRequest;
import com.study.ticket.domain.order.controller.dto.OrderResponse;
import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * 주문 컨트롤러
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문을 생성합니다.
     * @param request 주문 생성 요청
     * @return 생성된 주문
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderCreateRequest request) {
        log.info("주문 생성 요청: {}", request);
        
        Order order = orderService.createOrder(
                request.getUserId(),
                request.getScreeningId(),
                request.getSeatNumbers()
        );
        
        OrderResponse response = OrderResponse.from(order);
        return ResponseEntity
                .created(URI.create("/api/orders/" + order.getId()))
                .body(response);
    }

    /**
     * 주문을 조회합니다.
     * @param orderId 주문 ID
     * @return 주문
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        log.info("주문 조회 요청: {}", orderId);
        
        Order order = orderService.getOrder(orderId);
        OrderResponse response = OrderResponse.from(order);
        return ResponseEntity.ok(response);
    }

    /**
     * 주문을 취소합니다.
     * @param orderId 주문 ID
     * @return 취소된 주문
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId) {
        log.info("주문 취소 요청: {}", orderId);
        
        Order order = orderService.cancelOrder(orderId);
        OrderResponse response = OrderResponse.from(order);
        return ResponseEntity.ok(response);
    }
}
