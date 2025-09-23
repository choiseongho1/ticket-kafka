package com.study.ticket.domain.event.service;

import com.study.ticket.domain.event.domain.entity.ProcessedEvent;
import com.study.ticket.domain.event.domain.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 처리된 이벤트 서비스
 * 이벤트의 멱등성을 보장하기 위해 이미 처리된 이벤트를 기록하고 조회하는 역할을 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessedEventService {

    private final ProcessedEventRepository processedEventRepository;

    /**
     * 이벤트가 이미 처리되었는지 확인합니다.
     * @param eventKey 이벤트 키
     * @param consumerGroup 컨슈머 그룹
     * @return 이미 처리되었는지 여부
     */
    @Transactional(readOnly = true)
    public boolean isEventProcessed(String eventKey, String consumerGroup) {
        return processedEventRepository.existsByEventKeyAndConsumerGroup(eventKey, consumerGroup);
    }

    /**
     * 이벤트를 처리된 상태로 기록합니다.
     * 현재 트랜잭션에 참여합니다(REQUIRED).
     * @param eventKey 이벤트 키
     * @param eventType 이벤트 타입
     * @param consumerGroup 컨슈머 그룹
     * @return 처리된 이벤트
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public ProcessedEvent markEventAsProcessed(String eventKey, String eventType, String consumerGroup) {
        // 이미 처리된 이벤트인지 확인
        if (isEventProcessed(eventKey, consumerGroup)) {
            log.debug("이벤트가 이미 처리됨: {}, 컨슈머 그룹: {}", eventKey, consumerGroup);
            return processedEventRepository.findByEventKeyAndConsumerGroup(eventKey, consumerGroup).orElse(null);
        }

        // 처리된 이벤트 생성
        ProcessedEvent processedEvent = ProcessedEvent.builder()
                .eventKey(eventKey)
                .eventType(eventType)
                .consumerGroup(consumerGroup)
                .processedAt(LocalDateTime.now())
                .build();

        // 처리된 이벤트 저장
        return processedEventRepository.save(processedEvent);
    }

    /**
     * 이벤트를 처리된 상태로 기록합니다.
     * 새로운 트랜잭션에서 실행됩니다(REQUIRES_NEW).
     * @param eventKey 이벤트 키
     * @param eventType 이벤트 타입
     * @param consumerGroup 컨슈머 그룹
     * @return 처리된 이벤트
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ProcessedEvent markEventAsProcessedInNewTransaction(String eventKey, String eventType, String consumerGroup) {
        return markEventAsProcessed(eventKey, eventType, consumerGroup);
    }

    /**
     * 특정 기간 이전에 처리된 이벤트를 삭제합니다.
     * @param before 기준 시간
     * @return 삭제된 이벤트 수
     */
    @Transactional
    public int cleanupProcessedEvents(LocalDateTime before) {
        log.info("{}보다 이전에 처리된 이벤트 삭제", before);
        int deletedCount = processedEventRepository.deleteProcessedEventsBefore(before);
        log.info("삭제된 처리 이벤트 수: {}", deletedCount);
        return deletedCount;
    }
}
