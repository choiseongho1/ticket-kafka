package com.study.ticket.domain.movie.service;

import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.movie.domain.enums.Genre;
import com.study.ticket.domain.movie.domain.enums.Rating;
import com.study.ticket.domain.movie.domain.repository.MovieRepository;
import com.study.ticket.domain.movie.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Movie 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;

    /**
     * 영화를 생성합니다.
     * @param MovieSaveDto movieSaveDto
     * @return 생성된 영화
     */
    @Transactional
    public Movie createMovie(MovieSaveDto movieSaveDto) {
        // 영화 생성
        Movie movie = movieSaveDto.toEntity();
        
        // 영화 저장
        return movieRepository.save(movie);
    }

    /**
     * 영화를 조회합니다.
     * @param movieId 영화 ID
     * @return 영화
     */
    @Transactional(readOnly = true)
    public MovieDetailDto findMovieDetail(Long movieId) {
        return movieRepository.findMovieDetail(movieId)
                .orElseThrow(() -> new RuntimeException("영화를 찾을 수 없음: " + movieId));
    }

    /**
    * 영화 목록을 검색합니다.
    * @param MovieCondDto movieCondDt 검색 정보
    * @param pageable 페이지 정보
    * @return 영화 페이지
    */
   @Transactional(readOnly = true)
   public Page<MovieListDto> findMovieListWithPaging(MovieCondDto movieCondDto, Pageable pageable) {
       return movieRepository.findMovieListWithPaging(movieCondDto, pageable);
   }




    /**
     * 영화를 업데이트합니다.
     * @param movieId 영화 ID
     * @param MovieUpdateDto movieUpdateDto
     * @return 업데이트된 영화
     */
    @Transactional
    public Movie updateMovie(Long movieId, MovieUpdateDto movieUpdateDto) {

        // 영화 조회
        Movie movie = movieRepository.findById(movieId).orElseThrow();

        movieUpdateDto.toEntity(movie);

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
        Movie movie = movieRepository.findById(movieId).orElseThrow();

        // 영화 삭제
        movieRepository.delete(movie);
    }
}
