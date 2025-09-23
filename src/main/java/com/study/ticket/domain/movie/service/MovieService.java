package com.study.ticket.domain.movie.service;

import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.movie.domain.enums.Genre;
import com.study.ticket.domain.movie.domain.enums.Rating;
import com.study.ticket.domain.movie.domain.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 영화 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;

    /**
     * 영화를 생성합니다.
     * @param title 영화 제목
     * @param description 영화 설명
     * @param director 감독
     * @param actors 배우
     * @param runningTime 러닝 타임 (분)
     * @param releaseDate 개봉일
     * @param endDate 종료일
     * @param genre 장르
     * @param rating 등급
     * @return 생성된 영화
     */
    @Transactional
    public Movie createMovie(String title, String description, String director, String actors,
                           Integer runningTime, LocalDate releaseDate, LocalDate endDate,
                           Genre genre, Rating rating) {
        // 영화 생성
        Movie movie = Movie.builder()
                .title(title)
                .description(description)
                .director(director)
                .actors(actors)
                .runningTime(runningTime)
                .releaseDate(releaseDate)
                .endDate(endDate)
                .genre(genre)
                .rating(rating)
                .build();
        
        // 영화 저장
        return movieRepository.save(movie);
    }

    /**
     * 영화를 조회합니다.
     * @param movieId 영화 ID
     * @return 영화
     */
    @Transactional(readOnly = true)
    public Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("영화를 찾을 수 없음: " + movieId));
    }

    /**
     * 모든 영화를 조회합니다.
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    @Transactional(readOnly = true)
    public Page<Movie> getMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    /**
     * 제목으로 영화를 검색합니다.
     * @param title 영화 제목
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    @Transactional(readOnly = true)
    public Page<Movie> searchMoviesByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContaining(title, pageable);
    }

    /**
     * 장르별 영화를 조회합니다.
     * @param genre 장르
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    @Transactional(readOnly = true)
    public Page<Movie> getMoviesByGenre(Genre genre, Pageable pageable) {
        return movieRepository.findByGenre(genre, pageable);
    }

    /**
     * 개봉 예정 영화를 조회합니다.
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    @Transactional(readOnly = true)
    public Page<Movie> getUpcomingMovies(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return movieRepository.findByReleaseDateAfter(today, pageable);
    }

    /**
     * 현재 상영 중인 영화를 조회합니다.
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    @Transactional(readOnly = true)
    public Page<Movie> getNowPlayingMovies(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return movieRepository.findByReleaseDateBeforeAndEndDateAfter(today, today, pageable);
    }

    /**
     * 영화를 업데이트합니다.
     * @param movieId 영화 ID
     * @param title 영화 제목
     * @param description 영화 설명
     * @param director 감독
     * @param actors 배우
     * @param runningTime 러닝 타임 (분)
     * @param releaseDate 개봉일
     * @param endDate 종료일
     * @param genre 장르
     * @param rating 등급
     * @return 업데이트된 영화
     */
    @Transactional
    public Movie updateMovie(Long movieId, String title, String description, String director, String actors,
                           Integer runningTime, LocalDate releaseDate, LocalDate endDate,
                           Genre genre, Rating rating) {
        // 영화 조회
        Movie movie = getMovie(movieId);
        
        // 영화 정보 업데이트
        if (title != null) {
            movie.setTitle(title);
        }
        if (description != null) {
            movie.setDescription(description);
        }
        if (director != null) {
            movie.setDirector(director);
        }
        if (actors != null) {
            movie.setActors(actors);
        }
        if (runningTime != null) {
            movie.setRunningTime(runningTime);
        }
        if (releaseDate != null) {
            movie.setReleaseDate(releaseDate);
        }
        if (endDate != null) {
            movie.setEndDate(endDate);
        }
        if (genre != null) {
            movie.setGenre(genre);
        }
        if (rating != null) {
            movie.setRating(rating);
        }
        
        // 영화 저장
        return movieRepository.save(movie);
    }

    /**
     * 영화를 삭제합니다.
     * @param movieId 영화 ID
     */
    @Transactional
    public void deleteMovie(Long movieId) {
        // 영화 조회
        Movie movie = getMovie(movieId);
        
        // 영화 삭제
        movieRepository.delete(movie);
    }
}
