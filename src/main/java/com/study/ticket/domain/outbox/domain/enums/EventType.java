package com.study.ticket.domain.outbox.domain.enums;

/**
 * 이벤트 타입을 나타내는 열거형
 */
public enum EventType {
    // 주문 관련 이벤트
    ORDER_CREATED,
    ORDER_UPDATED,
    ORDER_CANCELLED,
    ORDER_COMPLETED,
    
    // 결제 관련 이벤트
    PAYMENT_CREATED,
    PAYMENT_APPROVED,
    PAYMENT_FAILED,
    PAYMENT_CANCELLED,
    PAYMENT_REFUNDED,
    
    // 티켓 관련 이벤트
    TICKET_ISSUED,
    TICKET_USED,
    TICKET_CANCELLED,
    
    // 상영 관련 이벤트
    SCREENING_CREATED,
    SCREENING_UPDATED,
    SCREENING_CANCELLED
}
