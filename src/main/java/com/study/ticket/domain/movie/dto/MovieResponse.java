package com.study.ticket.domain.movie.dto;

import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.movie.domain.enums.Genre;
import com.study.ticket.domain.movie.domain.enums.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 영화 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {

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

    /**
     * 영화 엔티티를 DTO로 변환합니다.
     * @param movie 영화 엔티티
     * @return 영화 응답 DTO
     */
    public static MovieResponse from(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .director(movie.getDirector())
                .actors(movie.getActors())
                .runningTime(movie.getRunningTime())
                .releaseDate(movie.getReleaseDate())
                .endDate(movie.getEndDate())
                .genre(movie.getGenre())
                .rating(movie.getRating())
                .build();
    }
}
