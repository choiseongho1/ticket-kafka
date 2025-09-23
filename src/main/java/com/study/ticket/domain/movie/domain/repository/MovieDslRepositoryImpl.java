package com.study.ticket.domain.movie.domain.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.ticket.domain.movie.dto.*;
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

import static com.study.ticket.domain.movie.domain.entity.QMovie.movie;

/**
 * MemoDslRepository 구현체
 */
@Repository
@RequiredArgsConstructor
public class MovieDslRepositoryImpl implements MovieDslRepository {

    private final JPAQueryFactory queryFactory;


    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public Page<MovieListDto> findMovieListWithPaging(MovieCondDto movieCondDto, Pageable pageable) {
        // 전체 카운트 조회
        final Long totalCount = findMovieListCount(movieCondDto).fetchOne();

        // 데이터 조회
        final List<MovieListDto> content = findMovieList(movieCondDto)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(content, pageable, (totalCount==null)? 0 : totalCount);

    }

    // findMovieListWithPaging 전체 카운트 조회
    private JPAQuery<Long> findMovieListCount(MovieCondDto movieCondDto){
        return queryFactory
                .select(movie.count())
                .from(movie)
                .where(movieListSearchCondition(movieCondDto));
    }

    // findMovieListWithPaging 목록 조회
    private JPAQuery<MovieListDto> findMovieList(MovieCondDto movieCondDto){
        return queryFactory
                .select(new QMovieListDto(
                        movie.id,
                        movie.title,
                        movie.description,
                        movie.director,
                        movie.actors,
                        movie.runningTime,
                        movie.releaseDate,
                        movie.endDate,
                        movie.genre,
                        movie.rating
                ))
                .from(movie)
                .where(movieListSearchCondition(movieCondDto));
    }

    // findMovieListWithPaging 조회 조건
    private Predicate[] movieListSearchCondition(MovieCondDto movieCondDto){
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(QueryUtils.contains(movieCondDto.getTitle(), movie.title));
        predicates.add(QueryUtils.eq(movieCondDto.getGenre(), movie.genre));

        LocalDate today = LocalDate.now();
        if(QueryUtils.isYes(movieCondDto.getUpComingFlag())){
            predicates.add(QueryUtils.gt(today, movie.releaseDate));
        }

        if(QueryUtils.isYes(movieCondDto.getNowPlayingFlag())){
            predicates.add(QueryUtils.between(movie.releaseDate, movie.endDate, today));
        }

        return predicates.toArray(new Predicate[0]);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------//

    @Override
    public Optional<MovieDetailDto> findMovieDetail(Long id){
        // 영화 기본 정보 조회
        MovieDetailDto memoDetailDto = queryFactory
                .select(new QMovieDetailDto(
                        movie.id,
                        movie.title,
                        movie.description,
                        movie.director,
                        movie.actors,
                        movie.runningTime,
                        movie.releaseDate,
                        movie.endDate,
                        movie.genre,
                        movie.rating
                ))
                .from(movie)
                .where(movie.id.eq(id))
                .fetchOne();
        
        // 영화가 존재하지 않으면 빈 Optional 반환
        if (memoDetailDto == null) {
            return Optional.empty();
        }

        return Optional.of(memoDetailDto);
    }
    

}
