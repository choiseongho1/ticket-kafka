package com.study.ticket.domain.movie.domain.enums;

/**
 * 영화 등급 열거형
 */
public enum Rating {
    ALL("전체 관람가"),
    TWELVE("12세 이상 관람가"),
    FIFTEEN("15세 이상 관람가"),
    ADULT("청소년 관람불가"),
    RESTRICTED("제한상영가");

    private final String displayName;

    Rating(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
