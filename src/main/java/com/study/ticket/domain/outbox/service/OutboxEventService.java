package com.study.ticket.domain.outbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ticket.domain.outbox.domain.entity.OutboxEvent;
import com.study.ticket.domain.outbox.domain.enums.EventStatus;
import com.study.ticket.domain.outbox.domain.enums.EventType;
import com.study.ticket.domain.outbox.domain.repository.OutboxEventRepository;
import com.study.ticket.global.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Outbox 이벤트 서비스
 * 이벤트를 Outbox 테이블에 저장하는 역할을 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    /**
     * 이벤트를 Outbox 테이블에 저장합니다.
     * 현재 트랜잭션에 참여합니다(REQUIRED).
     * 
     * @param event 이벤트
     * @param aggregateType 집계 타입 (ORDER, PAYMENT 등)
     * @param aggregateId 집계 ID
     * @param topic 카프카 토픽
     * @return 저장된 Outbox 이벤트
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public OutboxEvent saveEvent(Event event, String aggregateType, String aggregateId, String topic) {
        try {
            // 이벤트를 JSON으로 직렬화
            String payload = objectMapper.writeValueAsString(event);
            
            // 이벤트 키 생성
            String eventKey = event.getEventKey();
            
            // 이미 존재하는 이벤트 키인지 확인
            if (outboxEventRepository.findByEventKey(eventKey).isPresent()) {
                log.warn("이벤트 키 중복: {}", eventKey);
                return null;
            }
            
            // Outbox 이벤트 생성
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .aggregateType(aggregateType)
                    .aggregateId(aggregateId)
                    .eventType(EventType.valueOf(event.getEventType()))
                    .eventKey(eventKey)
                    .payload(payload)
                    .status(EventStatus.CREATED)
                    .topic(topic)
                    .partitionKey(aggregateId) // 파티션 키로 집계 ID 사용
                    .build();
            
            // Outbox 이벤트 저장
            return outboxEventRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            log.error("이벤트 직렬화 실패: {}", e.getMessage(), e);
            throw new RuntimeException("이벤트 직렬화 실패", e);
        }
    }

    /**
     * 이벤트를 Outbox 테이블에 저장합니다.
     * 새로운 트랜잭션에서 실행됩니다(REQUIRES_NEW).
     * 
     * @param event 이벤트
     * @param aggregateType 집계 타입 (ORDER, PAYMENT 등)
     * @param aggregateId 집계 ID
     * @param topic 카프카 토픽
     * @return 저장된 Outbox 이벤트
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OutboxEvent saveEventInNewTransaction(Event event, String aggregateType, String aggregateId, String topic) {
        return saveEvent(event, aggregateType, aggregateId, topic);
    }

    /**
     * 이벤트를 발행 준비 상태로 변경합니다.
     * 
     * @param eventId 이벤트 ID
     * @return 업데이트된 Outbox 이벤트
     */
    @Transactional
    public OutboxEvent markAsReady(Long eventId) {
        OutboxEvent event = outboxEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("이벤트를 찾을 수 없음: " + eventId));
        event.markAsReady();
        return outboxEventRepository.save(event);
    }

    /**
     * 이벤트를 발행 중 상태로 변경합니다.
     * 
     * @param eventId 이벤트 ID
     * @return 업데이트된 Outbox 이벤트
     */
    @Transactional
    public OutboxEvent markAsPublishing(Long eventId) {
        OutboxEvent event = outboxEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("이벤트를 찾을 수 없음: " + eventId));
        event.markAsPublishing();
        return outboxEventRepository.save(event);
    }

    /**
     * 이벤트를 발행 완료 상태로 변경합니다.
     * 
     * @param eventId 이벤트 ID
     * @return 업데이트된 Outbox 이벤트
     */
    @Transactional
    public OutboxEvent markAsPublished(Long eventId) {
        OutboxEvent event = outboxEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("이벤트를 찾을 수 없음: " + eventId));
        event.markAsPublished();
        return outboxEventRepository.save(event);
    }

    /**
     * 이벤트를 실패 상태로 변경합니다.
     * 
     * @param eventId 이벤트 ID
     * @param errorMessage 오류 메시지
     * @return 업데이트된 Outbox 이벤트
     */
    @Transactional
    public OutboxEvent markAsFailed(Long eventId, String errorMessage) {
        OutboxEvent event = outboxEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("이벤트를 찾을 수 없음: " + eventId));
        event.markAsFailed(errorMessage);
        return outboxEventRepository.save(event);
    }

    /**
     * 이벤트를 DLQ 상태로 변경합니다.
     * 
     * @param eventId 이벤트 ID
     * @param errorMessage 오류 메시지
     * @return 업데이트된 Outbox 이벤트
     */
    @Transactional
    public OutboxEvent markAsDlq(Long eventId, String errorMessage) {
        OutboxEvent event = outboxEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("이벤트를 찾을 수 없음: " + eventId));
        event.markAsDlq(errorMessage);
        return outboxEventRepository.save(event);
    }
}
