package com.study.ticket.domain.payment.domain.enums;

/**
 * 결제 방법을 나타내는 열거형
 */
public enum PaymentMethod {
    /**
     * 신용카드
     */
    CREDIT_CARD,
    
    /**
     * 계좌이체
     */
    BANK_TRANSFER,
    
    /**
     * 가상계좌
     */
    VIRTUAL_ACCOUNT,
    
    /**
     * 모바일 결제
     */
    MOBILE_PAYMENT,
    
    /**
     * 간편 결제
     */
    EASY_PAYMENT
}
