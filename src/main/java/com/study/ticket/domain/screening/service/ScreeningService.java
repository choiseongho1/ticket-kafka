package com.study.ticket.domain.screening.service;

import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.movie.domain.repository.MovieRepository;
import com.study.ticket.domain.outbox.service.OutboxEventService;
import com.study.ticket.domain.screening.domain.entity.Screening;
import com.study.ticket.domain.screening.domain.repository.ScreeningRepository;
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
import java.util.List;

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
     * @param movieId 영화 ID
     * @param screenName 상영관 이름
     * @param startTime 상영 시작 시간
     * @param endTime 상영 종료 시간
     * @param totalSeats 총 좌석 수
     * @param price 티켓 가격
     * @return 생성된 상영
     */
    @Transactional
    public Screening createScreening(Long movieId, String screenName, LocalDateTime startTime, 
                                    LocalDateTime endTime, Integer totalSeats, Integer price) {
        // 영화 조회
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("영화를 찾을 수 없음: " + movieId));
        
        // 상영 시간 유효성 검사
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException("상영 시작 시간이 종료 시간보다 늦을 수 없습니다.");
        }
        
        // 상영 생성
        Screening screening = Screening.builder()
                .movie(movie)
                .screenName(screenName)
                .startTime(startTime)
                .endTime(endTime)
                .totalSeats(totalSeats)
                .availableSeats(totalSeats)
                .price(price)
                .build();
        
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
        
        log.info("상영 생성 완료: 영화={}, 상영관={}, 시작시간={}", 
                movie.getTitle(), screenName, startTime);
        return savedScreening;
    }
    
    /**
     * 상영을 조회합니다.
     * @param screeningId 상영 ID
     * @return 상영
     */
    @Transactional(readOnly = true)
    public Screening getScreening(Long screeningId) {
        return screeningRepository.findById(screeningId)
                .orElseThrow(() -> new RuntimeException("상영을 찾을 수 없음: " + screeningId));
    }
    
    /**
     * 영화별 상영 목록을 조회합니다.
     * @param movieId 영화 ID
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    @Transactional(readOnly = true)
    public Page<Screening> getScreeningsByMovie(Long movieId, Pageable pageable) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("영화를 찾을 수 없음: " + movieId));
        return screeningRepository.findByMovie(movie, pageable);
    }
    
    /**
     * 날짜별 상영 목록을 조회합니다.
     * @param date 날짜
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    @Transactional(readOnly = true)
    public Page<Screening> getScreeningsByDate(LocalDate date, Pageable pageable) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return screeningRepository.findByStartTimeBetween(startOfDay, endOfDay, pageable);
    }
    
    /**
     * 영화 및 날짜별 상영 목록을 조회합니다.
     * @param movieId 영화 ID
     * @param date 날짜
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    @Transactional(readOnly = true)
    public Page<Screening> getScreeningsByMovieAndDate(Long movieId, LocalDate date, Pageable pageable) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("영화를 찾을 수 없음: " + movieId));
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return screeningRepository.findByMovieAndStartTimeBetween(movie, startOfDay, endOfDay, pageable);
    }
    
    /**
     * 상영을 업데이트합니다.
     * @param screeningId 상영 ID
     * @param screenName 상영관 이름
     * @param startTime 상영 시작 시간
     * @param endTime 상영 종료 시간
     * @param price 티켓 가격
     * @return 업데이트된 상영
     */
    @Transactional
    public Screening updateScreening(Long screeningId, String screenName, 
                                   LocalDateTime startTime, LocalDateTime endTime, Integer price) {
        Screening screening = getScreening(screeningId);
        
        // 이미 예약된 좌석이 있는 경우 시간 변경 불가
        if (screening.getTotalSeats() > screening.getAvailableSeats()) {
            throw new RuntimeException("이미 예약된 좌석이 있어 시간을 변경할 수 없습니다.");
        }
        
        // 상영 시간 유효성 검사
        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new RuntimeException("상영 시작 시간이 종료 시간보다 늦을 수 없습니다.");
        }
        
        // 상영 정보 업데이트
        if (screenName != null) {
            screening.setScreenName(screenName);
        }
        if (startTime != null) {
            screening.setStartTime(startTime);
        }
        if (endTime != null) {
            screening.setEndTime(endTime);
        }
        if (price != null) {
            screening.setPrice(price);
        }
        
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
        return updatedScreening;
    }
    
    /**
     * 상영을 삭제합니다.
     * @param screeningId 상영 ID
     */
    @Transactional
    public void deleteScreening(Long screeningId) {
        Screening screening = getScreening(screeningId);
        
        // 이미 예약된 좌석이 있는 경우 삭제 불가
        if (screening.getTotalSeats() > screening.getAvailableSeats()) {
            throw new RuntimeException("이미 예약된 좌석이 있어 삭제할 수 없습니다.");
        }
        
        screeningRepository.delete(screening);
        log.info("상영 삭제 완료: ID={}", screeningId);
    }
}
