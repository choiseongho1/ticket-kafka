package com.study.ticket.domain.screening.controller;

import com.study.ticket.domain.screening.controller.dto.ScreeningCreateRequest;
import com.study.ticket.domain.screening.controller.dto.ScreeningResponse;
import com.study.ticket.domain.screening.controller.dto.ScreeningUpdateRequest;
import com.study.ticket.domain.screening.domain.entity.Screening;
import com.study.ticket.domain.screening.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

/**
 * 상영 컨트롤러
 */
@RestController
@RequestMapping("/api/screenings")
@RequiredArgsConstructor
@Slf4j
public class ScreeningController {

    private final ScreeningService screeningService;

    /**
     * 상영을 생성합니다.
     * @param request 상영 생성 요청
     * @return 생성된 상영
     */
    @PostMapping
    public ResponseEntity<ScreeningResponse> createScreening(@RequestBody ScreeningCreateRequest request) {
        log.info("상영 생성 요청: {}", request);
        
        Screening screening = screeningService.createScreening(
                request.getMovieId(),
                request.getScreenName(),
                request.getStartTime(),
                request.getEndTime(),
                request.getTotalSeats(),
                request.getPrice()
        );
        
        ScreeningResponse response = ScreeningResponse.from(screening);
        return ResponseEntity
                .created(URI.create("/api/screenings/" + screening.getId()))
                .body(response);
    }

    /**
     * 상영을 조회합니다.
     * @param screeningId 상영 ID
     * @return 상영
     */
    @GetMapping("/{screeningId}")
    public ResponseEntity<ScreeningResponse> getScreening(@PathVariable Long screeningId) {
        log.info("상영 조회 요청: {}", screeningId);
        
        Screening screening = screeningService.getScreening(screeningId);
        ScreeningResponse response = ScreeningResponse.from(screening);
        return ResponseEntity.ok(response);
    }

    /**
     * 영화별 상영 목록을 조회합니다.
     * @param movieId 영화 ID
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<Page<ScreeningResponse>> getScreeningsByMovie(
            @PathVariable Long movieId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        log.info("영화별 상영 목록 조회 요청: {}", movieId);
        
        Page<Screening> screenings = screeningService.getScreeningsByMovie(movieId, pageable);
        Page<ScreeningResponse> responses = screenings.map(ScreeningResponse::from);
        return ResponseEntity.ok(responses);
    }

    /**
     * 날짜별 상영 목록을 조회합니다.
     * @param date 날짜
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<Page<ScreeningResponse>> getScreeningsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        log.info("날짜별 상영 목록 조회 요청: {}", date);
        
        Page<Screening> screenings = screeningService.getScreeningsByDate(date, pageable);
        Page<ScreeningResponse> responses = screenings.map(ScreeningResponse::from);
        return ResponseEntity.ok(responses);
    }

    /**
     * 영화 및 날짜별 상영 목록을 조회합니다.
     * @param movieId 영화 ID
     * @param date 날짜
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    @GetMapping("/movie/{movieId}/date/{date}")
    public ResponseEntity<Page<ScreeningResponse>> getScreeningsByMovieAndDate(
            @PathVariable Long movieId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        log.info("영화 및 날짜별 상영 목록 조회 요청: {}, {}", movieId, date);
        
        Page<Screening> screenings = screeningService.getScreeningsByMovieAndDate(movieId, date, pageable);
        Page<ScreeningResponse> responses = screenings.map(ScreeningResponse::from);
        return ResponseEntity.ok(responses);
    }

    /**
     * 상영을 업데이트합니다.
     * @param screeningId 상영 ID
     * @param request 상영 업데이트 요청
     * @return 업데이트된 상영
     */
    @PutMapping("/{screeningId}")
    public ResponseEntity<ScreeningResponse> updateScreening(
            @PathVariable Long screeningId,
            @RequestBody ScreeningUpdateRequest request
    ) {
        log.info("상영 업데이트 요청: {}, {}", screeningId, request);
        
        Screening screening = screeningService.updateScreening(
                screeningId,
                request.getScreenName(),
                request.getStartTime(),
                request.getEndTime(),
                request.getPrice()
        );
        
        ScreeningResponse response = ScreeningResponse.from(screening);
        return ResponseEntity.ok(response);
    }

    /**
     * 상영을 삭제합니다.
     * @param screeningId 상영 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{screeningId}")
    public ResponseEntity<Void> deleteScreening(@PathVariable Long screeningId) {
        log.info("상영 삭제 요청: {}", screeningId);
        
        screeningService.deleteScreening(screeningId);
        return ResponseEntity.noContent().build();
    }
}
