package com.study.ticket.domain.outbox.domain.repository;

import com.study.ticket.domain.outbox.domain.entity.OutboxEvent;
import com.study.ticket.domain.outbox.domain.enums.EventStatus;
import com.study.ticket.domain.outbox.domain.enums.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

/**
 * Outbox 이벤트 리포지토리 인터페이스
 */
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    /**
     * 이벤트 키로 이벤트를 조회합니다.
     * @param eventKey 이벤트 키
     * @return 이벤트 (Optional)
     */
    Optional<OutboxEvent> findByEventKey(String eventKey);

    /**
     * 상태별로 이벤트를 조회합니다.
     * @param status 이벤트 상태
     * @param pageable 페이지 정보
     * @return 이벤트 페이지
     */
    Page<OutboxEvent> findByStatus(EventStatus status, Pageable pageable);

    /**
     * 발행 가능한 이벤트를 조회합니다.
     * @param limit 조회할 이벤트 수
     * @return 이벤트 목록
     */
    @Query("SELECT e FROM OutboxEvent e WHERE e.status IN ('CREATED', 'READY') OR (e.status = 'FAILED' AND e.retryCount < 3) ORDER BY e.createdAt ASC LIMIT :limit")
    List<OutboxEvent> findPublishableEvents(@Param("limit") int limit);

    /**
     * 발행 가능한 이벤트를 비관적 락으로 조회합니다.
     * @param limit 조회할 이벤트 수
     * @return 이벤트 목록
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM OutboxEvent e WHERE e.status IN ('CREATED', 'READY') OR (e.status = 'FAILED' AND e.retryCount < 3) ORDER BY e.createdAt ASC LIMIT :limit")
    List<OutboxEvent> findPublishableEventsWithLock(@Param("limit") int limit);

    /**
     * 집계 ID와 이벤트 타입으로 이벤트를 조회합니다.
     * @param aggregateId 집계 ID
     * @param eventType 이벤트 타입
     * @return 이벤트 목록
     */
    List<OutboxEvent> findByAggregateIdAndEventType(String aggregateId, EventType eventType);

    /**
     * 집계 타입별로 이벤트를 조회합니다.
     * @param aggregateType 집계 타입
     * @param pageable 페이지 정보
     * @return 이벤트 페이지
     */
    Page<OutboxEvent> findByAggregateType(String aggregateType, Pageable pageable);

    /**
     * 토픽별로 이벤트를 조회합니다.
     * @param topic 토픽
     * @param pageable 페이지 정보
     * @return 이벤트 페이지
     */
    Page<OutboxEvent> findByTopic(String topic, Pageable pageable);
}
