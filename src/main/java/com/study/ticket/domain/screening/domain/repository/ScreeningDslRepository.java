package com.study.ticket.domain.screening.domain.repository;

import com.study.ticket.domain.movie.dto.MovieCondDto;
import com.study.ticket.domain.movie.dto.MovieListDto;
import com.study.ticket.domain.screening.dto.ScreeningCondDto;
import com.study.ticket.domain.screening.dto.ScreeningDetailDto;
import com.study.ticket.domain.screening.dto.ScreeningListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ScreeningDslRepository {

    Page<ScreeningListDto> findScreeningListWithPaging(ScreeningCondDto screeningCondDto, Pageable pageable);


    Optional<ScreeningDetailDto> findScreeningDetail(Long id);
}
