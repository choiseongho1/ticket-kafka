package com.study.ticket.global;

import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.movie.domain.enums.Genre;
import com.study.ticket.domain.movie.domain.enums.Rating;
import com.study.ticket.domain.movie.domain.repository.MovieRepository;
import com.study.ticket.domain.screening.domain.entity.Screening;
import com.study.ticket.domain.screening.domain.repository.ScreeningRepository;
import com.study.ticket.domain.user.domain.entity.User;
import com.study.ticket.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * 애플리케이션 시작 시 초기 데이터를 로드하는 클래스
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInit {

    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    private final UserRepository userRepository;

    /**
     * 개발 환경에서만 실행되는 초기 데이터 로드 메서드
     * @return CommandLineRunner 인스턴스
     */
    @Bean
    @Profile({"dev", "default"}) // 개발 환경과 기본 환경에서만 실행
    public CommandLineRunner initData() {
        return args -> {
            log.info("초기 데이터 로딩 시작");
            
            // 사용자 데이터 초기화
            initUsers();
            
            // 영화 데이터 초기화
            List<Movie> movies = initMovies();
            
            // 상영 데이터 초기화
            initScreenings(movies);
            
            log.info("초기 데이터 로딩 완료");
        };
    }

    /**
     * 사용자 데이터 초기화
     */
    private void initUsers() {
        log.info("사용자 데이터 초기화 시작");
        
        // 테스트 사용자 생성
        User user1 = User.builder()
                .name("홍길동")
                .email("user1@example.com")
                .password("password1")
                .phone("010-1234-5678")
                .role("USER")
                .enabled(true)
                .build();
        
        User user2 = User.builder()
                .name("김철수")
                .email("user2@example.com")
                .password("password2")
                .phone("010-2345-6789")
                .role("USER")
                .enabled(true)
                .build();
        
        // 관리자 계정
        User admin = User.builder()
                .name("관리자")
                .email("admin@example.com")
                .password("admin123")
                .phone("010-9876-5432")
                .role("ADMIN")
                .enabled(true)
                .build();
        
        userRepository.saveAll(Arrays.asList(user1, user2, admin));
        
        log.info("사용자 데이터 초기화 완료: {} 명", userRepository.count());
    }

    /**
     * 영화 데이터 초기화
     * @return 생성된 영화 목록
     */
    private List<Movie> initMovies() {
        log.info("영화 데이터 초기화 시작");
        
        // 현재 상영 중인 영화들
        Movie movie1 = Movie.builder()
                .title("어벤져스: 엔드게임")
                .description("인피니티 워 이후 절반만 살아남은 지구, 마지막 희망이 된 어벤져스")
                .director("앤서니 루소, 조 루소")
                .actors("로버트 다우니 주니어, 크리스 에반스, 크리스 헴스워스")
                .runningTime(181)
                .releaseDate(LocalDate.now().minusDays(30))
                .endDate(LocalDate.now().plusDays(30))
                .genre(Genre.ACTION)
                .rating(Rating.TWELVE)
                .build();
        
        Movie movie2 = Movie.builder()
                .title("기생충")
                .description("전원백수로 살 길 막막하지만 사이는 좋은 기택(송강호) 가족.")
                .director("봉준호")
                .actors("송강호, 이선균, 조여정, 최우식")
                .runningTime(132)
                .releaseDate(LocalDate.now().minusDays(15))
                .endDate(LocalDate.now().plusDays(45))
                .genre(Genre.DRAMA)
                .rating(Rating.FIFTEEN)
                .build();
        
        // 개봉 예정 영화
        Movie movie3 = Movie.builder()
                .title("듄: 파트 2")
                .description("사막 행성 아라키스에서 벌어지는 대서사시")
                .director("드니 빌뇌브")
                .actors("티모시 샬라메, 젠데이아, 레베카 퍼거슨")
                .runningTime(155)
                .releaseDate(LocalDate.now().plusDays(15))
                .endDate(LocalDate.now().plusDays(75))
                .genre(Genre.SCIENCE_FICTION)
                .rating(Rating.TWELVE)
                .build();
        
        // 상영 종료 예정 영화
        Movie movie4 = Movie.builder()
                .title("미션 임파서블: 데드 레코닝")
                .description("가장 위험한 작전, 그의 마지막 선택")
                .director("크리스토퍼 맥쿼리")
                .actors("톰 크루즈, 헤일리 앳웰, 사이먼 페그")
                .runningTime(163)
                .releaseDate(LocalDate.now().minusDays(60))
                .endDate(LocalDate.now().plusDays(5))
                .genre(Genre.ACTION)
                .rating(Rating.FIFTEEN)
                .build();
        
        // 공포 영화
        Movie movie5 = Movie.builder()
                .title("콰이어트 플레이스: 파트 3")
                .description("소리 내면 죽는다")
                .director("마이클 사노스키")
                .actors("루피타 뇽오, 조셉 퀸, 알렉스 울프")
                .runningTime(125)
                .releaseDate(LocalDate.now().minusDays(45))
                .endDate(LocalDate.now().plusDays(15))
                .genre(Genre.HORROR)
                .rating(Rating.FIFTEEN)
                .build();
        
        List<Movie> movies = movieRepository.saveAll(Arrays.asList(movie1, movie2, movie3, movie4, movie5));
        
        log.info("영화 데이터 초기화 완료: {} 편", movieRepository.count());
        
        return movies;
    }

    /**
     * 상영 데이터 초기화
     * @param movies 영화 목록
     */
    private void initScreenings(List<Movie> movies) {
        log.info("상영 데이터 초기화 시작");
        
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        
        // 오늘 상영 - 어벤져스: 엔드게임
        createScreeningsForMovie(movies.get(0), today, 
                Arrays.asList(
                        LocalTime.of(10, 0), 
                        LocalTime.of(13, 30), 
                        LocalTime.of(17, 0), 
                        LocalTime.of(20, 30)
                ),
                Arrays.asList("1관", "2관", "1관", "2관"),
                Arrays.asList(12000, 14000, 14000, 16000)
        );
        
        // 오늘 상영 - 기생충
        createScreeningsForMovie(movies.get(1), today,
                Arrays.asList(
                        LocalTime.of(11, 30),
                        LocalTime.of(14, 30),
                        LocalTime.of(18, 0)
                ),
                Arrays.asList("3관", "3관", "3관"),
                Arrays.asList(12000, 14000, 14000)
        );
        
        // 내일 상영 - 어벤져스: 엔드게임
        createScreeningsForMovie(movies.get(0), tomorrow,
                Arrays.asList(
                        LocalTime.of(10, 30),
                        LocalTime.of(14, 0),
                        LocalTime.of(17, 30),
                        LocalTime.of(21, 0)
                ),
                Arrays.asList("1관", "2관", "1관", "2관"),
                Arrays.asList(12000, 14000, 14000, 16000)
        );
        
        // 내일 상영 - 기생충
        createScreeningsForMovie(movies.get(1), tomorrow,
                Arrays.asList(
                        LocalTime.of(12, 0),
                        LocalTime.of(15, 0),
                        LocalTime.of(18, 30)
                ),
                Arrays.asList("3관", "3관", "3관"),
                Arrays.asList(12000, 14000, 14000)
        );
        
        // 내일 상영 - 미션 임파서블
        createScreeningsForMovie(movies.get(3), tomorrow,
                Arrays.asList(
                        LocalTime.of(11, 0),
                        LocalTime.of(14, 30),
                        LocalTime.of(18, 0)
                ),
                Arrays.asList("4관", "4관", "4관"),
                Arrays.asList(12000, 14000, 14000)
        );
        
        // 내일 상영 - 콰이어트 플레이스
        createScreeningsForMovie(movies.get(4), tomorrow,
                Arrays.asList(
                        LocalTime.of(13, 0),
                        LocalTime.of(16, 0),
                        LocalTime.of(19, 0),
                        LocalTime.of(22, 0)
                ),
                Arrays.asList("5관", "5관", "5관", "5관"),
                Arrays.asList(12000, 14000, 14000, 12000)
        );
        
        log.info("상영 데이터 초기화 완료: {} 개", screeningRepository.count());
    }
    
    /**
     * 영화별 상영 정보 생성
     * @param movie 영화
     * @param date 상영 날짜
     * @param startTimes 시작 시간 목록
     * @param screenNames 상영관 이름 목록
     * @param prices 가격 목록
     */
    private void createScreeningsForMovie(Movie movie, LocalDate date, List<LocalTime> startTimes, 
                                         List<String> screenNames, List<Integer> prices) {
        for (int i = 0; i < startTimes.size(); i++) {
            LocalTime startTime = startTimes.get(i);
            String screenName = screenNames.get(i);
            Integer price = prices.get(i);
            
            // 영화 러닝타임을 기준으로 종료 시간 계산
            LocalTime endTime = startTime.plusMinutes(movie.getRunningTime());
            
            Screening screening = Screening.builder()
                    .movie(movie)
                    .screenName(screenName)
                    .startTime(LocalDateTime.of(date, startTime))
                    .endTime(LocalDateTime.of(date, endTime))
                    .totalSeats(100)
                    .availableSeats(100)
                    .price(price)
                    .build();
            
            screeningRepository.save(screening);
        }
    }
}
