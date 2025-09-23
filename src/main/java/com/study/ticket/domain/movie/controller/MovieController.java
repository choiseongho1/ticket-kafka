package com.study.ticket.domain.movie.controller;

import com.study.ticket.domain.movie.controller.dto.MovieCreateRequest;
import com.study.ticket.domain.movie.controller.dto.MovieResponse;
import com.study.ticket.domain.movie.controller.dto.MovieUpdateRequest;
import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.movie.domain.enums.Genre;
import com.study.ticket.domain.movie.service.MovieService;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 영화 컨트롤러
 */
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;

    /**
     * 영화를 생성합니다.
     * @param request 영화 생성 요청
     * @return 생성된 영화
     */
    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@RequestBody MovieCreateRequest request) {
        log.info("영화 생성 요청: {}", request);
        
        Movie movie = movieService.createMovie(
                request.getTitle(),
                request.getDescription(),
                request.getDirector(),
                request.getActors(),
                request.getRunningTime(),
                request.getReleaseDate(),
                request.getEndDate(),
                request.getGenre(),
                request.getRating()
        );
        
        MovieResponse response = MovieResponse.from(movie);
        return ResponseEntity
                .created(URI.create("/api/movies/" + movie.getId()))
                .body(response);
    }

    /**
     * 영화를 조회합니다.
     * @param movieId 영화 ID
     * @return 영화
     */
    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResponse> getMovie(@PathVariable Long movieId) {
        log.info("영화 조회 요청: {}", movieId);
        
        Movie movie = movieService.getMovie(movieId);
        MovieResponse response = MovieResponse.from(movie);
        return ResponseEntity.ok(response);
    }

    /**
     * 모든 영화를 조회합니다.
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    @GetMapping
    public ResponseEntity<Page<MovieResponse>> getMovies(@PageableDefault(size = 10) Pageable pageable) {
        log.info("영화 목록 조회 요청");
        
        Page<Movie> movies = movieService.getMovies(pageable);
        Page<MovieResponse> responses = movies.map(MovieResponse::from);
        return ResponseEntity.ok(responses);
    }

    /**
     * 제목으로 영화를 검색합니다.
     * @param title 영화 제목
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    @GetMapping("/search")
    public ResponseEntity<Page<MovieResponse>> searchMoviesByTitle(
            @RequestParam String title,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        log.info("영화 제목 검색 요청: {}", title);
        
        Page<Movie> movies = movieService.searchMoviesByTitle(title, pageable);
        Page<MovieResponse> responses = movies.map(MovieResponse::from);
        return ResponseEntity.ok(responses);
    }

    /**
     * 장르별 영화를 조회합니다.
     * @param genre 장르
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<MovieResponse>> getMoviesByGenre(
            @PathVariable Genre genre,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        log.info("장르별 영화 목록 조회 요청: {}", genre);
        
        Page<Movie> movies = movieService.getMoviesByGenre(genre, pageable);
        Page<MovieResponse> responses = movies.map(MovieResponse::from);
        return ResponseEntity.ok(responses);
    }

    /**
     * 개봉 예정 영화를 조회합니다.
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    @GetMapping("/upcoming")
    public ResponseEntity<Page<MovieResponse>> getUpcomingMovies(@PageableDefault(size = 10) Pageable pageable) {
        log.info("개봉 예정 영화 목록 조회 요청");
        
        Page<Movie> movies = movieService.getUpcomingMovies(pageable);
        Page<MovieResponse> responses = movies.map(MovieResponse::from);
        return ResponseEntity.ok(responses);
    }

    /**
     * 현재 상영 중인 영화를 조회합니다.
     * @param pageable 페이지 정보
     * @return 영화 페이지
     */
    @GetMapping("/now-playing")
    public ResponseEntity<Page<MovieResponse>> getNowPlayingMovies(@PageableDefault(size = 10) Pageable pageable) {
        log.info("현재 상영 중인 영화 목록 조회 요청");
        
        Page<Movie> movies = movieService.getNowPlayingMovies(pageable);
        Page<MovieResponse> responses = movies.map(MovieResponse::from);
        return ResponseEntity.ok(responses);
    }

    /**
     * 영화를 업데이트합니다.
     * @param movieId 영화 ID
     * @param request 영화 업데이트 요청
     * @return 업데이트된 영화
     */
    @PutMapping("/{movieId}")
    public ResponseEntity<MovieResponse> updateMovie(
            @PathVariable Long movieId,
            @RequestBody MovieUpdateRequest request
    ) {
        log.info("영화 업데이트 요청: {}, {}", movieId, request);
        
        Movie movie = movieService.updateMovie(
                movieId,
                request.getTitle(),
                request.getDescription(),
                request.getDirector(),
                request.getActors(),
                request.getRunningTime(),
                request.getReleaseDate(),
                request.getEndDate(),
                request.getGenre(),
                request.getRating()
        );
        
        MovieResponse response = MovieResponse.from(movie);
        return ResponseEntity.ok(response);
    }

    /**
     * 영화를 삭제합니다.
     * @param movieId 영화 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        log.info("영화 삭제 요청: {}", movieId);
        
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }
}
