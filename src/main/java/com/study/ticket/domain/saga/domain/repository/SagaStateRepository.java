package com.study.ticket.domain.saga.domain.repository;

import com.study.ticket.domain.saga.domain.entity.SagaState;
import com.study.ticket.domain.saga.domain.enums.SagaStatus;
import com.study.ticket.domain.saga.domain.enums.SagaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Saga 상태 리포지토리 인터페이스
 */
public interface SagaStateRepository extends JpaRepository<SagaState, Long> {

    /**
     * Saga 키로 Saga 상태를 조회합니다.
     * @param sagaKey Saga 키
     * @return Saga 상태 (Optional)
     */
    Optional<SagaState> findBySagaKey(String sagaKey);

    /**
     * Saga 키로 Saga 상태를 비관적 락으로 조회합니다.
     * @param sagaKey Saga 키
     * @return Saga 상태 (Optional)
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SagaState s WHERE s.sagaKey = :sagaKey")
    Optional<SagaState> findBySagaKeyWithLock(@Param("sagaKey") String sagaKey);

    /**
     * Saga 타입과 상태로 Saga 상태 목록을 조회합니다.
     * @param sagaType Saga 타입
     * @param status Saga 상태
     * @param pageable 페이지 정보
     * @return Saga 상태 페이지
     */
    Page<SagaState> findBySagaTypeAndStatus(SagaType sagaType, SagaStatus status, Pageable pageable);

    /**
     * 특정 시간 이후에 시작되었지만 완료되지 않은 Saga 상태 목록을 조회합니다.
     * @param startTime 시작 시간
     * @return Saga 상태 목록
     */
    @Query("SELECT s FROM SagaState s WHERE s.createdAt > :startTime AND s.status NOT IN ('COMPLETED', 'COMPENSATED')")
    List<SagaState> findUncompletedSagasStartedAfter(@Param("startTime") LocalDateTime startTime);

    /**
     * 특정 시간 이후에 실패한 Saga 상태 목록을 조회합니다.
     * @param startTime 시작 시간
     * @return Saga 상태 목록
     */
    @Query("SELECT s FROM SagaState s WHERE s.createdAt > :startTime AND s.status = 'FAILED'")
    List<SagaState> findFailedSagasStartedAfter(@Param("startTime") LocalDateTime startTime);
}
