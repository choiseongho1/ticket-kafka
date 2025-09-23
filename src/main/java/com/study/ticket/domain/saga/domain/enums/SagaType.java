package com.study.ticket.domain.saga.domain.enums;

/**
 * Saga 타입을 나타내는 열거형
 */
public enum SagaType {
    /**
     * 주문 처리 Saga
     */
    ORDER_PROCESSING,
    
    /**
     * 결제 처리 Saga
     */
    PAYMENT_PROCESSING,
    
    /**
     * 티켓 발급 Saga
     */
    TICKET_ISSUANCE,
    
    /**
     * 주문 취소 Saga
     */
    ORDER_CANCELLATION,
    
    /**
     * 결제 환불 Saga
     */
    PAYMENT_REFUND
}
