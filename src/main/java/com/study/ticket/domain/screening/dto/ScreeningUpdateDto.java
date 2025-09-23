package com.study.ticket.domain.screening.dto;

import com.study.ticket.domain.screening.domain.entity.Screening;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 상영 업데이트 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningUpdateDto {

    /**
     * 상영관 이름
     */
    private String screenName;

    /**
     * 상영 시작 시간
     */
    private LocalDateTime startTime;

    /**
     * 상영 종료 시간
     */
    private LocalDateTime endTime;

    /**
     * 티켓 가격
     */
    private Integer price;


    public void toEntity(Screening screening){
        if(!StringUtils.isEmpty(screenName)) screening.setScreenName(screenName);
        if(!Objects.isNull(startTime)) screening.setStartTime(startTime);
        if(!Objects.isNull(endTime)) screening.setEndTime(endTime);
        if(!Objects.isNull(price)) screening.setPrice(price);
    }
}
