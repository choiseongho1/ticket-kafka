package com.study.ticket.domain.movie.domain.entity;

import com.study.ticket.domain.movie.domain.enums.Genre;
import com.study.ticket.domain.movie.domain.enums.Rating;
import com.study.ticket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

/**
 * 영화 엔티티
 */
@Entity
@Table(name = "MOVIES")
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_ID")
    @Comment("영화 ID")
    private Long id;

    @Column(name = "TITLE", nullable = false)
    @Comment("영화 제목")
    private String title;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    @Comment("영화 설명")
    private String description;

    @Column(name = "DIRECTOR")
    @Comment("감독")
    private String director;

    @Column(name = "ACTORS")
    @Comment("배우")
    private String actors;

    @Column(name = "RUNNING_TIME")
    @Comment("러닝 타임 (분)")
    private Integer runningTime;

    @Column(name = "RELEASE_DATE")
    @Comment("개봉일")
    private LocalDate releaseDate;

    @Column(name = "END_DATE")
    @Comment("종료일")
    private LocalDate endDate;

    @Column(name = "GENRE")
    @Enumerated(EnumType.STRING)
    @Comment("장르")
    private Genre genre;

    @Column(name = "RATING")
    @Enumerated(EnumType.STRING)
    @Comment("등급")
    private Rating rating;
}
