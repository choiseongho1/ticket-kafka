package com.study.ticket.domain.payment.domain.enums;

/**
 * 결제 상태를 나타내는 열거형
 */
public enum PaymentStatus {
    /**
     * 결제 대기 중
     */
    PENDING,
    
    /**
     * 결제 처리 중
     */
    PROCESSING,
    
    /**
     * 결제 완료됨
     */
    COMPLETED,
    
    /**
     * 결제 실패함
     */
    FAILED,
    
    /**
     * 결제 취소됨
     */
    CANCELLED,
    
    /**
     * 환불 처리됨
     */
    REFUNDED
}
