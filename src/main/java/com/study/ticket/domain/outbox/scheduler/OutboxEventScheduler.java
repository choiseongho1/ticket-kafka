package com.study.ticket.domain.outbox.scheduler;

import com.study.ticket.domain.outbox.service.OutboxEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Outbox 이벤트 스케줄러
 * 주기적으로 이벤트를 발행하는 역할을 담당합니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventScheduler {

    private final OutboxEventPublisher outboxEventPublisher;
    
    /**
     * 5초마다 이벤트를 발행합니다.
     */
    @Scheduled(fixedDelayString = "${outbox.scheduler.publish.interval:5000}")
    public void publishEvents() {
        try {
            log.debug("이벤트 발행 스케줄러 실행");
            int publishedCount = outboxEventPublisher.publishEvents(100);
            if (publishedCount > 0) {
                log.info("스케줄러에 의해 발행된 이벤트: {} 개", publishedCount);
            }
        } catch (Exception e) {
            log.error("이벤트 발행 스케줄러 실행 중 오류 발생: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 1분마다 실패한 이벤트를 재시도합니다.
     */
    @Scheduled(fixedDelayString = "${outbox.scheduler.retry.interval:60000}")
    public void retryFailedEvents() {
        try {
            log.debug("실패한 이벤트 재시도 스케줄러 실행");
            int retriedCount = outboxEventPublisher.retryFailedEvents(3);
            if (retriedCount > 0) {
                log.info("스케줄러에 의해 재시도된 이벤트: {} 개", retriedCount);
            }
        } catch (Exception e) {
            log.error("실패한 이벤트 재시도 스케줄러 실행 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
