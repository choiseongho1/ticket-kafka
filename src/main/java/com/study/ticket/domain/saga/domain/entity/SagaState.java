package com.study.ticket.domain.saga.domain.entity;

import com.study.ticket.domain.saga.domain.enums.SagaStatus;
import com.study.ticket.domain.saga.domain.enums.SagaType;
import com.study.ticket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * Saga 상태 엔티티
 * 분산 트랜잭션의 상태를 관리하는 엔티티
 */
@Entity
@Table(name = "SAGA_STATES")
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class SagaState extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SAGA_ID")
    @Comment("Saga ID")
    private Long id;

    @Column(name = "SAGA_KEY", nullable = false, unique = true)
    @Comment("Saga 키")
    private String sagaKey;

    @Column(name = "SAGA_TYPE", nullable = false)
    @Comment("Saga 타입")
    @Enumerated(EnumType.STRING)
    private SagaType sagaType;

    @Column(name = "CURRENT_STEP", nullable = false)
    @Comment("현재 단계")
    private Integer currentStep;

    @Column(name = "TOTAL_STEPS", nullable = false)
    @Comment("전체 단계")
    private Integer totalSteps;

    @Column(name = "STATUS", nullable = false)
    @Comment("상태")
    @Enumerated(EnumType.STRING)
    private SagaStatus status;

    @Column(name = "PAYLOAD", columnDefinition = "TEXT")
    @Comment("페이로드 (JSON)")
    private String payload;

    @Column(name = "ERROR_MESSAGE")
    @Comment("오류 메시지")
    private String errorMessage;

    @Column(name = "COMPLETED_AT")
    @Comment("완료 시간")
    private LocalDateTime completedAt;

    @Version
    @Column(name = "VERSION")
    @Comment("버전 (낙관적 락)")
    private Long version;

    /**
     * Saga를 생성합니다.
     */
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = SagaStatus.STARTED;
        }
        if (currentStep == null) {
            currentStep = 0;
        }
    }

    /**
     * 다음 단계로 진행합니다.
     * @return 모든 단계 완료 여부
     */
    public boolean nextStep() {
        this.currentStep++;
        if (this.currentStep >= this.totalSteps) {
            this.status = SagaStatus.COMPLETED;
            this.completedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

    /**
     * Saga를 실패 상태로 변경합니다.
     * @param errorMessage 오류 메시지
     */
    public void fail(String errorMessage) {
        this.status = SagaStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    /**
     * 보상 트랜잭션을 시작합니다.
     */
    public void startCompensation() {
        this.status = SagaStatus.COMPENSATING;
    }

    /**
     * 보상 트랜잭션을 완료합니다.
     */
    public void completeCompensation() {
        this.status = SagaStatus.COMPENSATED;
        this.completedAt = LocalDateTime.now();
    }

    /**
     * Saga가 완료되었는지 확인합니다.
     * @return 완료 여부
     */
    public boolean isCompleted() {
        return this.status == SagaStatus.COMPLETED;
    }

    /**
     * Saga가 실패했는지 확인합니다.
     * @return 실패 여부
     */
    public boolean isFailed() {
        return this.status == SagaStatus.FAILED;
    }

    /**
     * Saga가 보상 중인지 확인합니다.
     * @return 보상 중 여부
     */
    public boolean isCompensating() {
        return this.status == SagaStatus.COMPENSATING;
    }
}
