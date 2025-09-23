package com.study.ticket.domain.event.domain.repository;

import com.study.ticket.domain.event.domain.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 처리된 이벤트 리포지토리 인터페이스
 */
public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, Long> {

    /**
     * 이벤트 키와 컨슈머 그룹으로 처리된 이벤트를 조회합니다.
     * @param eventKey 이벤트 키
     * @param consumerGroup 컨슈머 그룹
     * @return 처리된 이벤트 (Optional)
     */
    Optional<ProcessedEvent> findByEventKeyAndConsumerGroup(String eventKey, String consumerGroup);

    /**
     * 이벤트 키와 컨슈머 그룹으로 이벤트가 처리되었는지 확인합니다.
     * @param eventKey 이벤트 키
     * @param consumerGroup 컨슈머 그룹
     * @return 처리 여부
     */
    boolean existsByEventKeyAndConsumerGroup(String eventKey, String consumerGroup);

    /**
     * 특정 기간 이전에 처리된 이벤트를 삭제합니다.
     * @param before 기준 시간
     * @return 삭제된 이벤트 수
     */
    @Query("DELETE FROM ProcessedEvent p WHERE p.processedAt < :before")
    int deleteProcessedEventsBefore(@Param("before") LocalDateTime before);
}
