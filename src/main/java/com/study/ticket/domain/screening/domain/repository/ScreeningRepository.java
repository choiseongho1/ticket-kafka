package com.study.ticket.domain.screening.domain.repository;

import com.study.ticket.domain.screening.domain.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상영 리포지토리 인터페이스
 */
public interface ScreeningRepository extends JpaRepository<Screening, Long>, ScreeningDslRepository {


}
