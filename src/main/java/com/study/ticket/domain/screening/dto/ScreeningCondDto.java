package com.study.ticket.domain.screening.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 상영 조회 조건 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningCondDto {


    /**
     * 영화 ID
     */
    private Long movieId;

    /**
     * 날짜
     */
    private LocalDate date;
}
