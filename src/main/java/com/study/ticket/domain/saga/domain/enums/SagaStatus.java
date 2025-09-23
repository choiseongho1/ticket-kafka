package com.study.ticket.domain.saga.domain.enums;

/**
 * Saga 상태를 나타내는 열거형
 */
public enum SagaStatus {
    /**
     * 시작됨
     */
    STARTED,
    
    /**
     * 진행 중
     */
    IN_PROGRESS,
    
    /**
     * 완료됨
     */
    COMPLETED,
    
    /**
     * 실패함
     */
    FAILED,
    
    /**
     * 보상 중
     */
    COMPENSATING,
    
    /**
     * 보상 완료됨
     */
    COMPENSATED
}
