package com.study.ticket.domain.ticket.domain.enums;

/**
 * 티켓 상태를 나타내는 열거형
 */
public enum TicketStatus {
    /**
     * 발급됨
     */
    ISSUED,
    
    /**
     * 사용됨
     */
    USED,
    
    /**
     * 취소됨
     */
    CANCELLED,
    
    /**
     * 만료됨
     */
    EXPIRED
}
