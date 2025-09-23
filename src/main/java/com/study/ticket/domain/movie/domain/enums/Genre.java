package com.study.ticket.domain.movie.domain.enums;

/**
 * 영화 장르 열거형
 */
public enum Genre {
    ACTION("액션"),
    ADVENTURE("모험"),
    ANIMATION("애니메이션"),
    COMEDY("코미디"),
    CRIME("범죄"),
    DOCUMENTARY("다큐멘터리"),
    DRAMA("드라마"),
    FAMILY("가족"),
    FANTASY("판타지"),
    HISTORY("역사"),
    HORROR("공포"),
    MUSIC("음악"),
    MYSTERY("미스터리"),
    ROMANCE("로맨스"),
    SCIENCE_FICTION("SF"),
    THRILLER("스릴러"),
    WAR("전쟁"),
    WESTERN("서부");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
