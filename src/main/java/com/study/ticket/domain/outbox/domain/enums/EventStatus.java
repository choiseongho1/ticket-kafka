package com.study.ticket.domain.outbox.domain.enums;

/**
 * 이벤트 상태를 나타내는 열거형
 */
public enum EventStatus {
    /**
     * 생성됨
     */
    CREATED,
    
    /**
     * 발행 준비됨
     */
    READY,
    
    /**
     * 발행 중
     */
    PUBLISHING,
    
    /**
     * 발행 완료됨
     */
    PUBLISHED,
    
    /**
     * 발행 실패함
     */
    FAILED,
    
    /**
     * DLQ(Dead Letter Queue)로 이동됨
     */
    DLQ
}
