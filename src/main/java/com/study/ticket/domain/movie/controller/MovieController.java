package com.study.ticket.domain.movie.controller;

import com.study.ticket.domain.movie.dto.*;
import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.movie.domain.enums.Genre;
import com.study.ticket.domain.movie.service.MovieService;
import com.study.ticket.global.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * 영화 컨트롤러
 */
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    // 성공 응답 코드
    private static final String MOVIE_CREATE_SUCCESS = "MOVIE_CREATE_SUCCESS";
    private static final String MOVIE_GET_SUCCESS = "MOVIE_GET_SUCCESS";
    private static final String MOVIE_LIST_SUCCESS = "MOVIE_LIST_SUCCESS";
    private static final String MOVIE_UPDATE_SUCCESS = "MOVIE_UPDATE_SUCCESS";
    private static final String MOVIE_DELETE_SUCCESS = "MOVIE_DELETE_SUCCESS";
    
    // 실패 응답 코드
    private static final String MOVIE_CREATE_FAILED = "MOVIE_CREATE_FAILED";
    private static final String MOVIE_GET_FAILED = "MOVIE_GET_FAILED";
    private static final String MOVIE_LIST_FAILED = "MOVIE_LIST_FAILED";
    private static final String MOVIE_UPDATE_FAILED = "MOVIE_UPDATE_FAILED";
    private static final String MOVIE_DELETE_FAILED = "MOVIE_DELETE_FAILED";

    private final MovieService movieService;

    /**
     * 영화를 생성합니다.
     * @param movieSaveDto 영화 생성 요청
     * @return 생성된 영화
     */
    @PostMapping
    public ResponseEntity<ResponseDto<?>> createMovie(@RequestBody MovieSaveDto movieSaveDto) {
        try {
            log.info("영화 생성 요청: {}", movieSaveDto);
            movieService.createMovie(movieSaveDto);

            ResponseDto<?> response = ResponseDto.builder()
                .responseCode(MOVIE_CREATE_SUCCESS)
                .responseMessage("영화가 성공적으로 생성되었습니다.")
                .build();
    
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("영화 생성 실패: {}", e.getMessage(), e);
            
            ResponseDto<?> errorResponse = ResponseDto.builder()
                .responseCode(MOVIE_CREATE_FAILED)
                .responseMessage("영화 생성에 실패했습니다: " + e.getMessage())
                .build();
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 영화를 조회합니다.
     * @param movieId 영화 ID
     * @return 영화
     */
    @GetMapping("/{movieId}")
    public ResponseEntity<ResponseDto<?>> findMovieDetail(@PathVariable Long movieId) {
        try {
            log.info("영화 조회 요청: {}", movieId);
            MovieDetailDto detail = movieService.findMovieDetail(movieId);

            ResponseDto<?> response = ResponseDto.builder()
                .responseCode(MOVIE_GET_SUCCESS)
                .responseMessage("영화 조회에 성공했습니다.")
                .data(detail)
                .build();
    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("영화 조회 실패: {}", e.getMessage(), e);
            
            ResponseDto<?> errorResponse = ResponseDto.builder()
                .responseCode(MOVIE_GET_FAILED)
                .responseMessage("영화 조회에 실패했습니다: " + e.getMessage())
                .build();
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    /**
     * 영화 목록을 조회합니다.
     * @param movieCondDto 영화 검색 조건
     * @param pageable 페이지 정보
     * @return 영화 목록
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<Page<MovieListDto>>> findMovieListWithPaging(@ModelAttribute MovieCondDto movieCondDto, @PageableDefault Pageable pageable) {
        try {
            log.info("영화 목록 조회 요청: {}", movieCondDto.toString());
            
            Page<MovieListDto> list = movieService.findMovieListWithPaging(movieCondDto, pageable);
            
            ResponseDto<Page<MovieListDto>> response = ResponseDto.<Page<MovieListDto>>builder()
                .responseCode(MOVIE_LIST_SUCCESS)
                .responseMessage("영화 목록 조회에 성공했습니다.")
                .data(list)
                .build();
    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("영화 목록 조회 실패: {}", e.getMessage(), e);
            
            ResponseDto<Page<MovieListDto>> errorResponse = ResponseDto.<Page<MovieListDto>>builder()
                .responseCode(MOVIE_LIST_FAILED)
                .responseMessage("영화 목록 조회에 실패했습니다: " + e.getMessage())
                .build();
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 영화를 업데이트합니다.
     * @param movieId 영화 ID
     * @param request 영화 업데이트 요청
     * @return 업데이트된 영화
     */
    @PutMapping("/{movieId}")
    public ResponseEntity<ResponseDto<?>> updateMovie(
            @PathVariable Long movieId,
            @RequestBody MovieUpdateDto movieUpdateDto
    ) {
        try {
            log.info("영화 업데이트 요청: {}, {}", movieId, movieUpdateDto);

            movieService.updateMovie(movieId, movieUpdateDto);

            ResponseDto<?> response = ResponseDto.builder()
                .responseCode(MOVIE_UPDATE_SUCCESS)
                .responseMessage("영화 업데이트에 성공했습니다.")
                .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("영화 업데이트 실패: {}", e.getMessage(), e);

            ResponseDto<?> errorResponse = ResponseDto.builder()
                .responseCode(MOVIE_UPDATE_FAILED)
                .responseMessage("영화 업데이트에 실패했습니다: " + e.getMessage())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 영화를 삭제합니다.
     * @param movieId 영화 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{movieId}")
    public ResponseEntity<ResponseDto<?>> deleteMovie(@PathVariable Long movieId) {
        try {
            log.info("영화 삭제 요청: {}", movieId);

            movieService.deleteMovie(movieId);

            ResponseDto<?> response = ResponseDto.builder()
                .responseCode(MOVIE_DELETE_SUCCESS)
                .responseMessage("영화 삭제에 성공했습니다.")
                .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("영화 삭제 실패: {}", e.getMessage(), e);

            ResponseDto<?> errorResponse = ResponseDto.builder()
                .responseCode(MOVIE_DELETE_FAILED)
                .responseMessage("영화 삭제에 실패했습니다: " + e.getMessage())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
