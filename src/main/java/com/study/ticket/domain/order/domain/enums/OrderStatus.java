package com.study.ticket.domain.order.domain.enums;

/**
 * 주문 상태를 나타내는 열거형
 */
public enum OrderStatus {
    /**
     * 주문 생성됨
     */
    CREATED,
    
    /**
     * 결제 대기 중
     */
    PAYMENT_PENDING,
    
    /**
     * 결제 완료됨
     */
    PAYMENT_COMPLETED,
    
    /**
     * 티켓 발급 완료됨
     */
    TICKET_ISSUED,
    
    /**
     * 주문 완료됨
     */
    COMPLETED,
    
    /**
     * 주문 취소됨
     */
    CANCELLED,
    
    /**
     * 주문 실패함
     */
    FAILED
}
