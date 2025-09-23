package com.study.ticket.domain.event.domain.entity;

import com.study.ticket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 처리된 이벤트 엔티티
 * 멱등성 처리를 위해 이미 처리된 이벤트를 기록하는 엔티티
 */
@Entity
@Table(name = "PROCESSED_EVENTS", indexes = {
    @Index(name = "idx_processed_events_event_key", columnList = "EVENT_KEY", unique = true)
})
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ProcessedEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROCESSED_EVENT_ID")
    @Comment("처리된 이벤트 ID")
    private Long id;

    @Column(name = "EVENT_KEY", nullable = false, unique = true)
    @Comment("이벤트 키 (멱등성 보장용)")
    private String eventKey;

    @Column(name = "EVENT_TYPE", nullable = false)
    @Comment("이벤트 타입")
    private String eventType;

    @Column(name = "CONSUMER_GROUP", nullable = false)
    @Comment("컨슈머 그룹")
    private String consumerGroup;

    @Column(name = "PROCESSED_AT", nullable = false)
    @Comment("처리 시간")
    private LocalDateTime processedAt;

    /**
     * 이벤트를 생성합니다.
     */
    @PrePersist
    public void prePersist() {
        if (processedAt == null) {
            processedAt = LocalDateTime.now();
        }
    }
}
