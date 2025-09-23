package com.study.ticket.domain.movie.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.movie.domain.enums.Genre;
import com.study.ticket.domain.movie.domain.enums.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 영화 목록 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
public class MovieListDto {

    /**
     * 영화 ID
     */
    private Long id;

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

    @QueryProjection
    public MovieListDto(Long id, String title, String description, String director, String actors, Integer runningTime, LocalDate releaseDate, LocalDate endDate, Genre genre, Rating rating){
        this.id = id;
        this.title = title;
        this.description = description;
        this.director = director;
        this.actors = actors;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
        this.endDate = endDate;
        this.genre = genre;
        this.rating = rating;
    }
}
