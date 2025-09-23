package com.study.ticket.domain.movie.domain.repository;

import com.study.ticket.domain.movie.dto.MovieCondDto;
import com.study.ticket.domain.movie.dto.MovieDetailDto;
import com.study.ticket.domain.movie.dto.MovieListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MovieDslRepository {

    Page<MovieListDto> findMovieListWithPaging(MovieCondDto movieCondDto, Pageable pageable);


    Optional<MovieDetailDto> findMovieDetail(Long id);
}
