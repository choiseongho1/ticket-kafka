package com.study.ticket.domain.screening.domain.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.ticket.domain.movie.dto.MovieCondDto;
import com.study.ticket.domain.movie.dto.MovieListDto;
import com.study.ticket.domain.movie.dto.QMovieListDto;
import com.study.ticket.domain.screening.dto.*;
import com.study.ticket.global.util.QueryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.study.ticket.domain.screening.domain.entity.QScreening.screening;

/**
 * MemoDslRepository 구현체
 */
@Repository
@RequiredArgsConstructor
public class ScreeningDslRepositoryImpl implements ScreeningDslRepository {

    private final JPAQueryFactory queryFactory;


    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public Page<ScreeningListDto> findScreeningListWithPaging(ScreeningCondDto screeningCondDto, Pageable pageable) {
        // 전체 카운트 조회
        final Long totalCount = findScreeningListCount(screeningCondDto).fetchOne();

        // 데이터 조회
        final List<ScreeningListDto> content = findScreeningList(screeningCondDto)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(content, pageable, (totalCount==null)? 0 : totalCount);

    }

    // findMovieListWithPaging 전체 카운트 조회
    private JPAQuery<Long> findScreeningListCount(ScreeningCondDto screeningCondDto){
        return queryFactory
                .select(screening.count())
                .from(screening)
                .where(screeningListSearchCondition(screeningCondDto));
    }

    // findMovieListWithPaging 목록 조회
    private JPAQuery<ScreeningListDto> findScreeningList(ScreeningCondDto screeningCondDto){
        return queryFactory
                .select(new QScreeningListDto(
                        screening.id,
                        screening.movie.id,
                        screening.movie.title,
                        screening.screenName,
                        screening.startTime,
                        screening.endTime,
                        screening.totalSeats,
                        screening.availableSeats,
                        screening.price
                ))
                .from(screening)
                .where(screeningListSearchCondition(screeningCondDto));
    }

    // findMovieListWithPaging 조회 조건
    private Predicate[] screeningListSearchCondition(ScreeningCondDto screeningCondDto){
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(QueryUtils.eq(screeningCondDto.getMovieId(), screening.movie.id));
        predicates.add(QueryUtils.between(screening.startTime, screening.endTime, screeningCondDto.getDate()));


        return predicates.toArray(new Predicate[0]);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public Optional<ScreeningDetailDto> findScreeningDetail(Long id){
        // 상영관 기본 정보 조회
        ScreeningDetailDto screeningDetail = queryFactory
                .select(new QScreeningDetailDto(
                        screening.id,
                        screening.movie.id,
                        screening.movie.title,
                        screening.screenName,
                        screening.startTime,
                        screening.endTime,
                        screening.totalSeats,
                        screening.availableSeats,
                        screening.price
                ))
                .from(screening)
                .where(screening.id.eq(id))
                .fetchOne();

        // 영화가 존재하지 않으면 빈 Optional 반환
        if (screeningDetail == null) {
            return Optional.empty();
        }

        return Optional.of(screeningDetail);
    }
    

}
