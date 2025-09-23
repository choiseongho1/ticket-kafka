package com.study.ticket.domain.movie.domain.repository;

import com.study.ticket.domain.movie.domain.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Movie 리포지토리 인터페이스
 */
public interface MovieRepository extends JpaRepository<Movie, Long>, MovieDslRepository{

}
