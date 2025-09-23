package com.study.ticket.domain.order.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ticket.domain.event.service.ProcessedEventService;
import com.study.ticket.domain.order.event.OrderCreatedEvent;
import com.study.ticket.global.event.AbstractEventListener;
import com.study.ticket.global.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 주문 이벤트 리스너
 * Kafka에서 주문 관련 이벤트를 수신하고 처리하는 역할을 담당합니다.
 */
@Component
@Slf4j
public class OrderEventListener extends AbstractEventListener {

    private static final String CONSUMER_GROUP = "order-service";

    public OrderEventListener(ProcessedEventService processedEventService, ObjectMapper objectMapper) {
        super(processedEventService, objectMapper);
    }

    /**
     * 주문 생성 이벤트를 수신합니다.
     * @param payload 이벤트 페이로드
     * @param key 메시지 키
     * @param partition 파티션
     * @param topic 토픽
     * @param acknowledgment 확인자
     */
    @KafkaListener(
            topics = "${kafka.topics.order-events}",
            groupId = CONSUMER_GROUP,
            containerFactory = "errorHandlingKafkaListenerContainerFactory"
    )
    public void consumeOrderCreatedEvent(
            @Payload String payload,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment acknowledgment
    ) {
        log.info("주문 이벤트 수신: topic={}, partition={}, key={}", topic, partition, key);
        processEvent(payload, OrderCreatedEvent.class, CONSUMER_GROUP, acknowledgment);
    }

    /**
     * 이벤트를 처리합니다.
     * @param event 이벤트
     * @throws Exception 이벤트 처리 중 발생한 예외
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Event> void handleEvent(T event) throws Exception {
        if (event instanceof OrderCreatedEvent) {
            handleOrderCreatedEvent((OrderCreatedEvent) event);
        } else {
            log.warn("알 수 없는 이벤트 타입: {}", event.getClass().getSimpleName());
        }
    }
    
    /**
     * 주문 생성 이벤트를 처리합니다.
     * @param event 주문 생성 이벤트
     */
    private void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("주문 생성 이벤트 처리: orderId={}, orderNumber={}", event.getOrderId(), event.getOrderNumber());
        
        // 여기에 주문 생성 이벤트 처리 로직 구현
        // 예: 결제 프로세스 시작, 알림 발송 등
        
        log.info("주문 생성 이벤트 처리 완료: orderId={}", event.getOrderId());
    }
}
