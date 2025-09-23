package com.study.ticket.domain.screening.service;

import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.movie.domain.repository.MovieRepository;
import com.study.ticket.domain.outbox.service.OutboxEventService;
import com.study.ticket.domain.screening.domain.entity.Screening;
import com.study.ticket.domain.screening.domain.repository.ScreeningRepository;
import com.study.ticket.domain.screening.dto.*;
import com.study.ticket.domain.screening.event.ScreeningCreatedEvent;
import com.study.ticket.domain.screening.event.ScreeningUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 상영 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final OutboxEventService outboxEventService;
    
    @Value("${kafka.topics.screening-events}")
    private String screeningEventsTopic;
    
    /**
     * 상영을 생성합니다.
     * @param ScreeningSaveDto screeningSaveDto
     */
    @Transactional
    public void createScreening(ScreeningSaveDto screeningSaveDto) {
        // 영화 조회
        Movie movie = movieRepository.findById(screeningSaveDto.getMovieId())
                .orElseThrow(() -> new RuntimeException("영화를 찾을 수 없음: " + screeningSaveDto.getMovieId()));
        
        // 상영 시간 유효성 검사
        if (screeningSaveDto.getStartTime().isAfter(screeningSaveDto.getEndTime())) {
            throw new RuntimeException("상영 시작 시간이 종료 시간보다 늦을 수 없습니다.");
        }
        
        // 상영 생성
        Screening screening = screeningSaveDto.toEntity(movie);
        
        // 상영 저장
        Screening savedScreening = screeningRepository.save(screening);
        
        // 상영 생성 이벤트 발행
        ScreeningCreatedEvent event = new ScreeningCreatedEvent(savedScreening);
        outboxEventService.saveEvent(
                event,
                "SCREENING",
                savedScreening.getId().toString(),
                screeningEventsTopic
        );
        
    }
    
    /**
     * 상영을 조회합니다.
     * @param screeningId 상영 ID
     * @return 상영
     */
    @Transactional(readOnly = true)
    public ScreeningDetailDto findScreeningDetail(Long screeningId) {
        return screeningRepository.findScreeningDetail(screeningId)
                .orElseThrow(() -> new RuntimeException("상영을 찾을 수 없음: " + screeningId));
    }


    /**
     * 상영 목록을 조회합니다.
     * @param ScreeningCondDto screeningCondDto
     * @param Pageable pageable
     * @return 상영
     */
    @Transactional(readOnly = true)
    public Page<ScreeningListDto> findScreeningListWithPaging(ScreeningCondDto screeningCondDto, Pageable pageable) {
        return screeningRepository.findScreeningListWithPaging(screeningCondDto, pageable);
    }

    
    /**
     * 상영을 업데이트합니다.
     * @param screeningId 상영 ID
     * @param ScreeningUpdateDto screeningUpdateDto
     */
    @Transactional
    public void updateScreening(Long screeningId, ScreeningUpdateDto screeningUpdateDto) {
        Screening screening = screeningRepository.findById(screeningId).orElseThrow();


        // 이미 예약된 좌석이 있는 경우 시간 변경 불가
        if (screening.getTotalSeats() > screening.getAvailableSeats()) {
            throw new RuntimeException("이미 예약된 좌석이 있어 시간을 변경할 수 없습니다.");
        }

        // 상영 시간 유효성 검사
        if (screeningUpdateDto.getStartTime() != null && screeningUpdateDto.getEndTime() != null
                && screeningUpdateDto.getStartTime().isAfter(screeningUpdateDto.getEndTime())) {
            throw new RuntimeException("상영 시작 시간이 종료 시간보다 늦을 수 없습니다.");
        }

        screeningUpdateDto.toEntity(screening);

        // 상영 저장
        Screening updatedScreening = screeningRepository.save(screening);

        // 상영 업데이트 이벤트 발행
        ScreeningUpdatedEvent event = new ScreeningUpdatedEvent(updatedScreening);
        outboxEventService.saveEvent(
                event,
                "SCREENING",
                updatedScreening.getId().toString(),
                screeningEventsTopic
        );

        log.info("상영 업데이트 완료: ID={}", screeningId);
    }

    /**
     * 상영을 삭제합니다.
     * @param screeningId 상영 ID
     */
    @Transactional
    public void deleteScreening(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId).orElseThrow();

        // 이미 예약된 좌석이 있는 경우 삭제 불가
        if (screening.getTotalSeats() > screening.getAvailableSeats()) {
            throw new RuntimeException("이미 예약된 좌석이 있어 삭제할 수 없습니다.");
        }

        screeningRepository.delete(screening);
        log.info("상영 삭제 완료: ID={}", screeningId);
    }
}
