package com.study.ticket.domain.movie.controller.dto;

import com.study.ticket.domain.movie.domain.enums.Genre;
import com.study.ticket.domain.movie.domain.enums.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 영화 업데이트 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieUpdateRequest {

    /**
     * 영화 제목
     */
    private String title;

    /**
     * 영화 설명
     */
    private String description;

    /**
     * 감독
     */
    private String director;

    /**
     * 배우
     */
    private String actors;

    /**
     * 러닝 타임 (분)
     */
    private Integer runningTime;

    /**
     * 개봉일
     */
    private LocalDate releaseDate;

    /**
     * 종료일
     */
    private LocalDate endDate;

    /**
     * 장르
     */
    private Genre genre;

    /**
     * 등급
     */
    private Rating rating;
}
