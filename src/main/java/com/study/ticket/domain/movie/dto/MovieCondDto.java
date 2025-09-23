package com.study.ticket.domain.movie.dto;

import com.study.ticket.domain.movie.domain.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 영화 조회 조건 DTO
 */
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class MovieCondDto {

    /**
     * 영화 제목
     */
    private String title;

    /**
     * 장르
     */
    private Genre genre;

    /**
     * 개봉 예정 여부
     */
    private String upComingFlag;

    /**
     * 상영 여부
     */
    private String nowPlayingFlag;



}
