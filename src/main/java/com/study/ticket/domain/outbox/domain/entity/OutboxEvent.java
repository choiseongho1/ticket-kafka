package com.study.ticket.domain.outbox.domain.entity;

import com.study.ticket.domain.outbox.domain.enums.EventStatus;
import com.study.ticket.domain.outbox.domain.enums.EventType;
import com.study.ticket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * Outbox 이벤트 엔티티
 * 트랜잭션 내에서 이벤트를 저장하고, 별도 프로세스가 Kafka로 발행하는 Outbox 패턴을 위한 엔티티
 */
@Entity
@Table(name = "OUTBOX_EVENTS")
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class OutboxEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    @Comment("이벤트 ID")
    private Long id;

    @Column(name = "AGGREGATE_ID", nullable = false)
    @Comment("집계 ID (주문 ID, 결제 ID 등)")
    private String aggregateId;

    @Column(name = "AGGREGATE_TYPE", nullable = false)
    @Comment("집계 타입 (ORDER, PAYMENT 등)")
    private String aggregateType;

    @Column(name = "EVENT_TYPE", nullable = false)
    @Comment("이벤트 타입")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(name = "EVENT_KEY", nullable = false)
    @Comment("이벤트 키 (멱등성 보장용)")
    private String eventKey;

    @Column(name = "PAYLOAD", nullable = false, columnDefinition = "TEXT")
    @Comment("이벤트 페이로드 (JSON)")
    private String payload;

    @Column(name = "STATUS", nullable = false)
    @Comment("이벤트 상태")
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "TOPIC", nullable = false)
    @Comment("대상 Kafka 토픽")
    private String topic;

    @Column(name = "PARTITION_KEY")
    @Comment("파티션 키")
    private String partitionKey;

    @Column(name = "PUBLISHED_AT")
    @Comment("발행 시간")
    private LocalDateTime publishedAt;

    @Column(name = "RETRY_COUNT")
    @Comment("재시도 횟수")
    private Integer retryCount;

    @Column(name = "ERROR_MESSAGE")
    @Comment("오류 메시지")
    private String errorMessage;

    /**
     * 이벤트를 생성합니다.
     */
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = EventStatus.CREATED;
        }
        if (retryCount == null) {
            retryCount = 0;
        }
    }

    /**
     * 이벤트를 발행 대기 상태로 변경합니다.
     */
    public void markAsReady() {
        this.status = EventStatus.READY;
    }

    /**
     * 이벤트를 발행 중 상태로 변경합니다.
     */
    public void markAsPublishing() {
        this.status = EventStatus.PUBLISHING;
    }

    /**
     * 이벤트를 발행 완료 상태로 변경합니다.
     */
    public void markAsPublished() {
        this.status = EventStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

    /**
     * 이벤트를 실패 상태로 변경합니다.
     * @param errorMessage 오류 메시지
     */
    public void markAsFailed(String errorMessage) {
        this.status = EventStatus.FAILED;
        this.errorMessage = errorMessage;
        this.retryCount++;
    }

    /**
     * 이벤트를 DLQ(Dead Letter Queue) 상태로 변경합니다.
     * @param errorMessage 오류 메시지
     */
    public void markAsDlq(String errorMessage) {
        this.status = EventStatus.DLQ;
        this.errorMessage = errorMessage;
    }

    /**
     * 이벤트가 발행 가능한지 확인합니다.
     * @return 발행 가능 여부
     */
    public boolean isPublishable() {
        return this.status == EventStatus.CREATED || 
               this.status == EventStatus.READY || 
               (this.status == EventStatus.FAILED && this.retryCount < 3);
    }
}
