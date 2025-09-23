package com.study.ticket.global.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ticket.domain.event.service.ProcessedEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;

/**
 * 추상 이벤트 리스너
 * 모든 이벤트 리스너의 공통 기능을 제공합니다.
 */
@Slf4j
public abstract class AbstractEventListener {

    protected final ProcessedEventService processedEventService;
    protected final ObjectMapper objectMapper;

    protected AbstractEventListener(ProcessedEventService processedEventService, ObjectMapper objectMapper) {
        this.processedEventService = processedEventService;
        this.objectMapper = objectMapper;
    }

    /**
     * 이벤트를 처리합니다.
     * @param payload 이벤트 페이로드
     * @param eventClass 이벤트 클래스
     * @param consumerGroup 컨슈머 그룹
     * @param acknowledgment 확인자
     * @param <T> 이벤트 타입
     */
    protected <T extends Event> void processEvent(
            String payload,
            Class<T> eventClass,
            String consumerGroup,
            Acknowledgment acknowledgment
    ) {
        try {
            // JSON을 이벤트 객체로 변환
            T event = objectMapper.readValue(payload, eventClass);
            
            // 이벤트 키 추출
            String eventKey = event.getEventKey();
            String eventType = event.getEventType();
            
            // 멱등성 검사
            if (processedEventService.isEventProcessed(eventKey, consumerGroup)) {
                log.info("이벤트 중복 수신, 무시: {}", eventKey);
                acknowledgment.acknowledge();
                return;
            }
            
            // 이벤트 처리
            handleEvent(event);
            
            // 이벤트 처리 완료 기록
            processedEventService.markEventAsProcessed(eventKey, eventType, consumerGroup);
            
            // 메시지 확인 (수동 커밋)
            acknowledgment.acknowledge();
            
        } catch (JsonProcessingException e) {
            log.error("이벤트 역직렬화 실패: {}", e.getMessage(), e);
            // JSON 파싱 오류는 재시도해도 동일한 오류가 발생하므로 ack
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("이벤트 처리 실패: {}", e.getMessage(), e);
            // 재시도를 위해 ack하지 않음
        }
    }

    /**
     * 이벤트를 처리합니다.
     * 구체적인 이벤트 처리 로직은 하위 클래스에서 구현합니다.
     * @param event 이벤트
     * @param <T> 이벤트 타입
     * @throws Exception 이벤트 처리 중 발생한 예외
     */
    protected abstract <T extends Event> void handleEvent(T event) throws Exception;
}
