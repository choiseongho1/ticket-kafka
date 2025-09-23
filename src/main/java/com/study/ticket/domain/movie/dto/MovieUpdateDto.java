package com.study.ticket.domain.movie.dto;

import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.movie.domain.enums.Genre;
import com.study.ticket.domain.movie.domain.enums.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 영화 업데이트 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieUpdateDto {

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


    public void toEntity(Movie movie) {
        if(!StringUtils.isEmpty(title)) movie.setTitle(title);
        if(!StringUtils.isEmpty(description)) movie.setDescription(description);
        if(!StringUtils.isEmpty(director)) movie.setDirector(director);
        if(!StringUtils.isEmpty(actors)) movie.setActors(actors);
        if(!Objects.isNull(runningTime)) movie.setRunningTime(runningTime);
        if(!Objects.isNull(releaseDate)) movie.setReleaseDate(releaseDate);
        if(!Objects.isNull(endDate)) movie.setEndDate(endDate);
        if(!Objects.isNull(genre)) movie.setGenre(genre);
        if(!Objects.isNull(rating)) movie.setRating(rating);
    }
}
