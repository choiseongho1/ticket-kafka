package com.study.ticket.global.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 기본 이벤트 클래스
 * 모든 이벤트의 공통 속성을 정의합니다.
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseEvent implements Event {

    /**
     * 이벤트 ID
     */
    private String eventId;

    /**
     * 이벤트 타입
     */
    private String eventType;

    /**
     * 이벤트 발생 시간
     */
    private LocalDateTime occurredAt;

    /**
     * 이벤트 키 (멱등성 보장용)
     */
    private String eventKey;

    /**
     * 이벤트 버전
     */
    private String eventVersion;

    /**
     * 기본 이벤트 생성자
     * @param eventType 이벤트 타입
     */
    protected BaseEvent(String eventType) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.occurredAt = LocalDateTime.now();
        this.eventVersion = "1.0";
    }

    /**
     * 이벤트 키를 생성합니다.
     * @param aggregateType 집계 타입
     * @param aggregateId 집계 ID
     * @return 이벤트 키
     */
    protected String generateEventKey(String aggregateType, String aggregateId) {
        return String.format("%s:%s:%s:%s", aggregateType, aggregateId, eventType, eventId);
    }
}
