package com.study.ticket.domain.movie.domain.repository;

import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.movie.domain.enums.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * 영화 리포지토리 인터페이스
 */
public interface MovieRepository extends JpaRepository<Movie, Long>, MovieDslRepository{

    /**
     * 제목으로 영화를 검색합니다.
     * @param title 영화 제목
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    Page<Movie> findByTitleContaining(String title, Pageable pageable);

    /**
     * 장르별 영화를 조회합니다.
     * @param genre 장르
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    Page<Movie> findByGenre(Genre genre, Pageable pageable);

    /**
     * 개봉일 이후의 영화를 조회합니다.
     * @param releaseDate 개봉일
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    Page<Movie> findByReleaseDateAfter(LocalDate releaseDate, Pageable pageable);


    /**
     * 개봉일 이전이고 종료일 이후의 영화를 조회합니다. (현재 상영 중인 영화)
     * @param releaseDate 개봉일
     * @param endDate 종료일
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    Page<Movie> findByReleaseDateBeforeAndEndDateAfter(LocalDate releaseDate, LocalDate endDate, Pageable pageable);

}
