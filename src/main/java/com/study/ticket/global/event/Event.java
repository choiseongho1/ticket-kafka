package com.study.ticket.global.event;

import java.time.LocalDateTime;

/**
 * 이벤트 인터페이스
 * 모든 이벤트는 이 인터페이스를 구현해야 합니다.
 */
public interface Event {
    /**
     * 이벤트 ID를 반환합니다.
     * @return 이벤트 ID
     */
    String getEventId();

    /**
     * 이벤트 타입을 반환합니다.
     * @return 이벤트 타입
     */
    String getEventType();

    /**
     * 이벤트 발생 시간을 반환합니다.
     * @return 이벤트 발생 시간
     */
    LocalDateTime getOccurredAt();

    /**
     * 이벤트 키를 반환합니다. (멱등성 보장용)
     * @return 이벤트 키
     */
    String getEventKey();

    /**
     * 이벤트 버전을 반환합니다.
     * @return 이벤트 버전
     */
    String getEventVersion();
}
