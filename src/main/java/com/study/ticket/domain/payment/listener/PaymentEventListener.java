package com.study.ticket.domain.payment.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ticket.domain.event.service.ProcessedEventService;
import com.study.ticket.domain.payment.event.PaymentApprovedEvent;
import com.study.ticket.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 결제 이벤트 리스너
 * Kafka에서 결제 관련 이벤트를 수신하고 처리하는 역할을 담당합니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final ProcessedEventService processedEventService;
    private final ObjectMapper objectMapper;
    private final TicketService ticketService;
    
    private static final String CONSUMER_GROUP = "payment-service";

    /**
     * 결제 이벤트를 수신합니다.
     * @param payload 이벤트 페이로드
     * @param key 메시지 키
     * @param partition 파티션
     * @param topic 토픽
     * @param acknowledgment 확인자
     */
    @KafkaListener(
            topics = "${kafka.topics.payment-events}",
            groupId = CONSUMER_GROUP,
            containerFactory = "errorHandlingKafkaListenerContainerFactory"
    )
    public void consumePaymentEvent(
            @Payload String payload,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment acknowledgment
    ) {
        log.info("결제 이벤트 수신: topic={}, partition={}, key={}", topic, partition, key);
        
        try {
            // JSON을 이벤트 객체로 변환
            PaymentApprovedEvent event = objectMapper.readValue(payload, PaymentApprovedEvent.class);
            
            // 이벤트 키 추출
            String eventKey = event.getEventKey();
            String eventType = event.getEventType();
            
            // 멱등성 검사
            if (processedEventService.isEventProcessed(eventKey, CONSUMER_GROUP)) {
                log.info("이벤트 중복 수신, 무시: {}", eventKey);
                acknowledgment.acknowledge();
                return;
            }
            
            // 이벤트 타입에 따른 처리
            switch (eventType) {
                case "PAYMENT_APPROVED":
                    handlePaymentApprovedEvent(event);
                    break;
                default:
                    log.warn("알 수 없는 이벤트 타입: {}", eventType);
            }
            
            // 이벤트 처리 완료 기록
            processedEventService.markEventAsProcessed(eventKey, eventType, CONSUMER_GROUP);
            
            // 메시지 확인 (수동 커밋)
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("이벤트 처리 실패: {}", e.getMessage(), e);
            // 재시도를 위해 ack하지 않음
        }
    }
    
    /**
     * 결제 승인 이벤트를 처리합니다.
     * @param event 결제 승인 이벤트
     */
    private void handlePaymentApprovedEvent(PaymentApprovedEvent event) {
        log.info("결제 승인 이벤트 처리: paymentId={}, orderId={}", event.getPaymentId(), event.getOrderId());
        
        try {
            // 결제 승인 후 티켓 발급 프로세스 시작
            ticketService.issueTickets(event.getOrderId());
            
            log.info("결제 승인 이벤트 처리 완료: paymentId={}", event.getPaymentId());
        } catch (Exception e) {
            log.error("결제 승인 이벤트 처리 실패: {}", e.getMessage(), e);
            throw e;
        }
    }
}
