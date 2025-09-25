package com.study.ticket.domain.ticket.dto;

import com.study.ticket.domain.ticket.domain.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 티켓 조회 조건 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketCondDto {

    /**
     * 티켓 상태
     */
    private TicketStatus status;
}
