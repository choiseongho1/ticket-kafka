package com.study.ticket.domain.outbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ticket.domain.outbox.domain.entity.OutboxEvent;
import com.study.ticket.domain.outbox.domain.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Outbox 이벤트 발행자
 * Outbox 테이블에서 이벤트를 조회하여 Kafka로 발행하는 역할을 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventService outboxEventService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 발행 가능한 이벤트를 조회하여 Kafka로 발행합니다.
     * @param batchSize 한 번에 처리할 이벤트 수
     * @return 발행된 이벤트 수
     */
    @Transactional
    public int publishEvents(int batchSize) {
        // 발행 가능한 이벤트 조회
        List<OutboxEvent> events = outboxEventRepository.findPublishableEventsWithLock(batchSize);
        
        if (events.isEmpty()) {
            log.debug("발행할 이벤트가 없습니다.");
            return 0;
        }
        
        log.info("이벤트 발행 시작: {} 개", events.size());
        int publishedCount = 0;
        
        for (OutboxEvent event : events) {
            try {
                // 이벤트 상태를 발행 중으로 변경
                outboxEventService.markAsPublishing(event.getId());
                
                // 이벤트 발행
                publishEvent(event);
                
                publishedCount++;
            } catch (Exception e) {
                log.error("이벤트 발행 실패: {}, 이벤트 ID: {}", e.getMessage(), event.getId(), e);
                outboxEventService.markAsFailed(event.getId(), e.getMessage());
                
                // 재시도 횟수가 3회 이상이면 DLQ로 이동
                if (event.getRetryCount() >= 3) {
                    outboxEventService.markAsDlq(event.getId(), "최대 재시도 횟수 초과: " + e.getMessage());
                }
            }
        }
        
        log.info("이벤트 발행 완료: {} 개", publishedCount);
        return publishedCount;
    }
    
    /**
     * 단일 이벤트를 Kafka로 발행합니다.
     * @param event Outbox 이벤트
     */
    private void publishEvent(OutboxEvent event) {
        try {
            // 이벤트 페이로드를 Object로 변환
            Object payload = objectMapper.readValue(event.getPayload(), Object.class);
            
            // Kafka로 이벤트 발행
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
                    event.getTopic(),
                    event.getPartitionKey(),
                    payload
            );
            
            // 비동기 콜백 처리
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    // 발행 성공
                    log.debug("이벤트 발행 성공: {}, 토픽: {}, 파티션: {}, 오프셋: {}",
                            event.getId(),
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                    
                    outboxEventService.markAsPublished(event.getId());
                } else {
                    // 발행 실패
                    log.error("이벤트 발행 실패: {}, 이벤트 ID: {}", ex.getMessage(), event.getId(), ex);
                    outboxEventService.markAsFailed(event.getId(), ex.getMessage());
                }
            });
            
            // 비동기 처리이므로 여기서는 결과를 기다리지 않음
        } catch (JsonProcessingException e) {
            log.error("이벤트 역직렬화 실패: {}, 이벤트 ID: {}", e.getMessage(), event.getId(), e);
            throw new RuntimeException("이벤트 역직렬화 실패", e);
        }
    }
    
    /**
     * 실패한 이벤트를 재시도합니다.
     * @param maxRetries 최대 재시도 횟수
     * @return 재시도된 이벤트 수
     */
    @Transactional
    public int retryFailedEvents(int maxRetries) {
        // 실패한 이벤트 중 재시도 횟수가 maxRetries 미만인 이벤트 조회
        List<OutboxEvent> failedEvents = outboxEventRepository.findAll().stream()
                .filter(e -> e.getStatus().equals(com.study.ticket.domain.outbox.domain.enums.EventStatus.FAILED))
                .filter(e -> e.getRetryCount() < maxRetries)
                .toList();
        
        if (failedEvents.isEmpty()) {
            return 0;
        }
        
        log.info("실패한 이벤트 재시도: {} 개", failedEvents.size());
        int retriedCount = 0;
        
        for (OutboxEvent event : failedEvents) {
            try {
                // 이벤트 상태를 발행 준비로 변경
                outboxEventService.markAsReady(event.getId());
                retriedCount++;
            } catch (Exception e) {
                log.error("이벤트 재시도 실패: {}, 이벤트 ID: {}", e.getMessage(), event.getId(), e);
            }
        }
        
        return retriedCount;
    }
}
