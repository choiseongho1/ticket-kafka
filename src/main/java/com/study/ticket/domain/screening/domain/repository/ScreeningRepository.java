package com.study.ticket.domain.screening.domain.repository;

import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.screening.domain.entity.Screening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 상영 리포지토리 인터페이스
 */
public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    /**
     * 영화별 상영 목록을 조회합니다.
     * @param movie 영화
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    Page<Screening> findByMovie(Movie movie, Pageable pageable);

    /**
     * 상영 시작 시간 범위로 상영 목록을 조회합니다.
     * @param startTime 시작 시간
     * @param endTime 종료 시간
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    Page<Screening> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 영화 및 상영 시작 시간 범위로 상영 목록을 조회합니다.
     * @param movie 영화
     * @param startTime 시작 시간
     * @param endTime 종료 시간
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    Page<Screening> findByMovieAndStartTimeBetween(Movie movie, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 특정 시간 이후에 시작하는 상영 목록을 조회합니다.
     * @param startTime 시작 시간
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    Page<Screening> findByStartTimeAfter(LocalDateTime startTime, Pageable pageable);

    /**
     * 특정 시간 이전에 종료되는 상영 목록을 조회합니다.
     * @param endTime 종료 시간
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    Page<Screening> findByEndTimeBefore(LocalDateTime endTime, Pageable pageable);

    /**
     * 가용 좌석이 있는 상영 목록을 조회합니다.
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    @Query("SELECT s FROM Screening s WHERE s.availableSeats > 0")
    Page<Screening> findWithAvailableSeats(Pageable pageable);

    /**
     * 영화 및 가용 좌석이 있는 상영 목록을 조회합니다.
     * @param movie 영화
     * @param pageable 페이지 정보
     * @return 상영 페이지
     */
    @Query("SELECT s FROM Screening s WHERE s.movie = :movie AND s.availableSeats > 0")
    Page<Screening> findByMovieWithAvailableSeats(@Param("movie") Movie movie, Pageable pageable);
}
