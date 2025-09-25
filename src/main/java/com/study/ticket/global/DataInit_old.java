package com.study.ticket.global;

import com.study.ticket.domain.movie.domain.entity.Movie;
import com.study.ticket.domain.movie.domain.enums.Genre;
import com.study.ticket.domain.movie.domain.enums.Rating;
import com.study.ticket.domain.movie.domain.repository.MovieRepository;
import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.order.domain.entity.OrderItem;
import com.study.ticket.domain.order.domain.enums.OrderStatus;
import com.study.ticket.domain.order.domain.repository.OrderRepository;
import com.study.ticket.domain.payment.domain.entity.Payment;
import com.study.ticket.domain.payment.domain.enums.PaymentMethod;
import com.study.ticket.domain.payment.domain.enums.PaymentStatus;
import com.study.ticket.domain.payment.domain.repository.PaymentRepository;
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
// @Configuration
// @RequiredArgsConstructor
// @Slf4j
public class DataInit_old {

//    private final MovieRepository movieRepository;
//    private final ScreeningRepository screeningRepository;
//    private final UserRepository userRepository;
//    private final OrderRepository orderRepository;
//    private final PaymentRepository paymentRepository;
//    /**
//     * 개발 환경에서만 실행되는 초기 데이터 로드 메서드
//     * @return CommandLineRunner 인스턴스
//     */
//    @Bean
//    @Profile({"dev", "default"}) // 개발 환경과 기본 환경에서만 실행
//    public CommandLineRunner initData() {
//        return args -> {
//            log.info("초기 데이터 로딩 시작");
//
//            // 사용자 데이터 초기화
//            List<User> users = initUsers();
//
//            // 영화 데이터 초기화
//            List<Movie> movies = initMovies();
//
//            // 상영 데이터 초기화
//            List<Screening> screenings = initScreenings(movies);
//
//            // 주문 데이터 초기화
//            initOrders(users, screenings);
//
//            // 결제 데이터 초기화
//            initPayments();
//
//            log.info("초기 데이터 로딩 완료");
//        };
//    }
//
//    /**
//     * 사용자 데이터 초기화
//     * @return 생성된 사용자 목록
//     */
//    private List<User> initUsers() {
//        log.info("사용자 데이터 초기화 시작");
//
//        // 테스트 사용자 생성
//        User user1 = User.builder()
//                .name("홍길동")
//                .email("user1@example.com")
//                .password("password1")
//                .phone("010-1234-5678")
//                .role("USER")
//                .enabled(true)
//                .build();
//
//        User user2 = User.builder()
//                .name("김철수")
//                .email("user2@example.com")
//                .password("password2")
//                .phone("010-2345-6789")
//                .role("USER")
//                .enabled(true)
//                .build();
//
//        // 관리자 계정
//        User admin = User.builder()
//                .name("관리자")
//                .email("admin@example.com")
//                .password("admin123")
//                .phone("010-9876-5432")
//                .role("ADMIN")
//                .enabled(true)
//                .build();
//
//        List<User> users = userRepository.saveAll(Arrays.asList(user1, user2, admin));
//
//        log.info("사용자 데이터 초기화 완료: {} 명", userRepository.count());
//
//        return users;
//    }
//
//    /**
//     * 영화 데이터 초기화
//     * @return 생성된 영화 목록
//     */
//    private List<Movie> initMovies() {
//        log.info("영화 데이터 초기화 시작");
//
//        // 현재 상영 중인 영화들
//        Movie movie1 = Movie.builder()
//                .title("어벤져스: 엔드게임")
//                .description("인피니티 워 이후 절반만 살아남은 지구, 마지막 희망이 된 어벤져스")
//                .director("앤서니 루소, 조 루소")
//                .actors("로버트 다우니 주니어, 크리스 에반스, 크리스 헴스워스")
//                .runningTime(181)
//                .releaseDate(LocalDate.now().minusDays(30))
//                .endDate(LocalDate.now().plusDays(30))
//                .genre(Genre.ACTION)
//                .rating(Rating.TWELVE)
//                .build();
//
//        Movie movie2 = Movie.builder()
//                .title("기생충")
//                .description("전원백수로 살 길 막막하지만 사이는 좋은 기택(송강호) 가족.")
//                .director("봉준호")
//                .actors("송강호, 이선균, 조여정, 최우식")
//                .runningTime(132)
//                .releaseDate(LocalDate.now().minusDays(15))
//                .endDate(LocalDate.now().plusDays(45))
//                .genre(Genre.DRAMA)
//                .rating(Rating.FIFTEEN)
//                .build();
//
//        // 개봉 예정 영화
//        Movie movie3 = Movie.builder()
//                .title("듄: 파트 2")
//                .description("사막 행성 아라키스에서 벌어지는 대서사시")
//                .director("드니 빌뇌브")
//                .actors("티모시 샬라메, 젠데이아, 레베카 퍼거슨")
//                .runningTime(155)
//                .releaseDate(LocalDate.now().plusDays(15))
//                .endDate(LocalDate.now().plusDays(75))
//                .genre(Genre.SCIENCE_FICTION)
//                .rating(Rating.TWELVE)
//                .build();
//
//        // 상영 종료 예정 영화
//        Movie movie4 = Movie.builder()
//                .title("미션 임파서블: 데드 레코닝")
//                .description("가장 위험한 작전, 그의 마지막 선택")
//                .director("크리스토퍼 맥쿼리")
//                .actors("톰 크루즈, 헤일리 앳웰, 사이먼 페그")
//                .runningTime(163)
//                .releaseDate(LocalDate.now().minusDays(60))
//                .endDate(LocalDate.now().plusDays(5))
//                .genre(Genre.ACTION)
//                .rating(Rating.FIFTEEN)
//                .build();
//
//        // 공포 영화
//        Movie movie5 = Movie.builder()
//                .title("콰이어트 플레이스: 파트 3")
//                .description("소리 내면 죽는다")
//                .director("마이클 사노스키")
//                .actors("루피타 뇽오, 조셉 퀸, 알렉스 울프")
//                .runningTime(125)
//                .releaseDate(LocalDate.now().minusDays(45))
//                .endDate(LocalDate.now().plusDays(15))
//                .genre(Genre.HORROR)
//                .rating(Rating.FIFTEEN)
//                .build();
//
//        List<Movie> movies = movieRepository.saveAll(Arrays.asList(movie1, movie2, movie3, movie4, movie5));
//
//        log.info("영화 데이터 초기화 완료: {} 편", movieRepository.count());
//
//        return movies;
//    }
//
//    /**
//     * 상영 데이터 초기화
//     * @param movies 영화 목록
//     * @return 생성된 상영 목록
//     */
//    private List<Screening> initScreenings(List<Movie> movies) {
//        log.info("상영 데이터 초기화 시작");
//
//        LocalDate today = LocalDate.now();
//        LocalDate tomorrow = today.plusDays(1);
//
//        // 오늘 상영 - 어벤져스: 엔드게임
//        createScreeningsForMovie(movies.get(0), today,
//                Arrays.asList(
//                        LocalTime.of(10, 0),
//                        LocalTime.of(13, 30),
//                        LocalTime.of(17, 0),
//                        LocalTime.of(20, 30)
//                ),
//                Arrays.asList("1관", "2관", "1관", "2관"),
//                Arrays.asList(12000, 14000, 14000, 16000)
//        );
//
//        // 오늘 상영 - 기생충
//        createScreeningsForMovie(movies.get(1), today,
//                Arrays.asList(
//                        LocalTime.of(11, 30),
//                        LocalTime.of(14, 30),
//                        LocalTime.of(18, 0)
//                ),
//                Arrays.asList("3관", "3관", "3관"),
//                Arrays.asList(12000, 14000, 14000)
//        );
//
//        // 내일 상영 - 어벤져스: 엔드게임
//        createScreeningsForMovie(movies.get(0), tomorrow,
//                Arrays.asList(
//                        LocalTime.of(10, 30),
//                        LocalTime.of(14, 0),
//                        LocalTime.of(17, 30),
//                        LocalTime.of(21, 0)
//                ),
//                Arrays.asList("1관", "2관", "1관", "2관"),
//                Arrays.asList(12000, 14000, 14000, 16000)
//        );
//
//        // 내일 상영 - 기생충
//        createScreeningsForMovie(movies.get(1), tomorrow,
//                Arrays.asList(
//                        LocalTime.of(12, 0),
//                        LocalTime.of(15, 0),
//                        LocalTime.of(18, 30)
//                ),
//                Arrays.asList("3관", "3관", "3관"),
//                Arrays.asList(12000, 14000, 14000)
//        );
//
//        // 내일 상영 - 미션 임파서블
//        createScreeningsForMovie(movies.get(3), tomorrow,
//                Arrays.asList(
//                        LocalTime.of(11, 0),
//                        LocalTime.of(14, 30),
//                        LocalTime.of(18, 0)
//                ),
//                Arrays.asList("4관", "4관", "4관"),
//                Arrays.asList(12000, 14000, 14000)
//        );
//
//        // 내일 상영 - 콰이어트 플레이스
//        createScreeningsForMovie(movies.get(4), tomorrow,
//                Arrays.asList(
//                        LocalTime.of(13, 0),
//                        LocalTime.of(16, 0),
//                        LocalTime.of(19, 0),
//                        LocalTime.of(22, 0)
//                ),
//                Arrays.asList("5관", "5관", "5관", "5관"),
//                Arrays.asList(12000, 14000, 14000, 12000)
//        );
//
//        log.info("상영 데이터 초기화 완료: {} 개", screeningRepository.count());
//
//        // 생성된 상영 정보 반환
//        return screeningRepository.findAll();
//    }
//
//    /**
//     * 영화별 상영 정보 생성
//     * @param movie 영화
//     * @param date 상영 날짜
//     * @param startTimes 시작 시간 목록
//     * @param screenNames 상영관 이름 목록
//     * @param prices 가격 목록
//     */
//    private void createScreeningsForMovie(Movie movie, LocalDate date, List<LocalTime> startTimes,
//                                         List<String> screenNames, List<Integer> prices) {
//        for (int i = 0; i < startTimes.size(); i++) {
//            LocalTime startTime = startTimes.get(i);
//            String screenName = screenNames.get(i);
//            Integer price = prices.get(i);
//
//            // 영화 러닝타임을 기준으로 종료 시간 계산
//            LocalTime endTime = startTime.plusMinutes(movie.getRunningTime());
//
//            Screening screening = Screening.builder()
//                    .movie(movie)
//                    .screenName(screenName)
//                    .startTime(LocalDateTime.of(date, startTime))
//                    .endTime(LocalDateTime.of(date, endTime))
//                    .totalSeats(100)
//                    .availableSeats(100)
//                    .price(price)
//                    .build();
//
//            screeningRepository.save(screening);
//        }
//    }
//
//    /**
//     * 주문 데이터 초기화
//     * @param users 사용자 목록
//     * @param screenings 상영 목록
//     */
//    private void initOrders(List<User> users, List<Screening> screenings) {
//        log.info("주문 데이터 초기화 시작");
//
//        // 이미 주문 데이터가 있는지 확인
//        if (orderRepository.count() > 0) {
//            log.info("주문 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
//            return;
//        }
//
//        // 첫 번째 사용자의 완료된 주문
//        createOrder(
//            users.get(0),
//            screenings.get(0),
//            "ORD-20250924-00001",
//            OrderStatus.COMPLETED,
//            Arrays.asList("A1", "A2"),
//            LocalDateTime.now().minusHours(2)
//        );
//
//        // 두 번째 사용자의 완료된 주문
//        createOrder(
//            users.get(1),
//            screenings.get(1),
//            "ORD-20250924-00002",
//            OrderStatus.COMPLETED,
//            Arrays.asList("B1", "B2", "B3"),
//            LocalDateTime.now().minusHours(1)
//        );
//
//        // 첫 번째 사용자의 결제 대기 중인 주문
//        createOrder(
//            users.get(0),
//            screenings.get(2),
//            "ORD-20250924-00003",
//            OrderStatus.PAYMENT_PENDING,
//            Arrays.asList("C1"),
//            LocalDateTime.now().minusMinutes(30)
//        );
//
//        // 두 번째 사용자의 생성된 주문
//        createOrder(
//            users.get(1),
//            screenings.get(3),
//            "ORD-20250924-00004",
//            OrderStatus.CREATED,
//            Arrays.asList("D1", "D2"),
//            LocalDateTime.now().minusMinutes(15)
//        );
//
//        // 첫 번째 사용자의 취소된 주문
//        createOrder(
//            users.get(0),
//            screenings.get(4),
//            "ORD-20250924-00005",
//            OrderStatus.CANCELLED,
//            Arrays.asList("E1", "E2"),
//            LocalDateTime.now().minusMinutes(45)
//        );
//
//        log.info("주문 데이터 초기화 완료: {} 개", orderRepository.count());
//    }
//
//    /**
//     * 주문 생성 헬퍼 메서드
//     * @param user 사용자
//     * @param screening 상영
//     * @param orderNumber 주문 번호
//     * @param status 주문 상태
//     * @param seatNumbers 좌석 번호 목록
//     * @param createdAt 생성 시간
//     */
//    private void createOrder(User user, Screening screening, String orderNumber, OrderStatus status, List<String> seatNumbers, LocalDateTime createdAt) {
//        // 주문 생성
//        Order order = Order.builder()
//                .orderNumber(orderNumber)
//                .user(user)
//                .screening(screening)
//                .seatCount(seatNumbers.size())
//                .totalAmount(screening.getPrice() * seatNumbers.size())
//                .status(status)
//                .paymentDeadline(createdAt.plusHours(3))
//                .build();
//
//        // 주문 항목 추가
//        for (String seatNumber : seatNumbers) {
//            OrderItem orderItem = OrderItem.builder()
//                    .seatNumber(seatNumber)
//                    .price(screening.getPrice())
//                    .build();
//            order.addOrderItem(orderItem);
//        }
//
//        // 상영 좌석 예약 처리 (취소된 주문은 제외)
//        if (status != OrderStatus.CANCELLED) {
//            screening.reserve(seatNumbers.size());
//            screeningRepository.save(screening);
//        }
//
//        // 주문 저장
//        orderRepository.save(order);
//    }
//
//    /**
//     * 결제 데이터 초기화
//     */
//    private void initPayments() {
//        log.info("결제 데이터 초기화 시작");
//
//        // 이미 결제 데이터가 있는지 확인
//        if (paymentRepository.count() > 0) {
//            log.info("결제 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
//            return;
//        }
//
//        // 첫 번째 결제 (완료된 주문)
//        createPayment(
//            orderRepository.findByOrderNumber("ORD-20250924-00001").orElseThrow(),
//            "PAY-20250924-00001",
//            PaymentMethod.EASY_PAYMENT,
//            PaymentStatus.COMPLETED,
//            LocalDateTime.now().minusHours(2)
//        );
//
//        // 두 번째 결제 (완료된 주문)
//        createPayment(
//            orderRepository.findByOrderNumber("ORD-20250924-00002").orElseThrow(),
//            "PAY-20250924-00002",
//            PaymentMethod.EASY_PAYMENT,
//            PaymentStatus.COMPLETED,
//            LocalDateTime.now().minusHours(1)
//        );
//
//        // 세 번째 결제 (결제 대기 중인 주문)
//        createPayment(
//            orderRepository.findByOrderNumber("ORD-20250924-00003").orElseThrow(),
//            "PAY-20250924-00003",
//            PaymentMethod.EASY_PAYMENT,
//            PaymentStatus.PENDING,
//            LocalDateTime.now().minusMinutes(30)
//        );
//
//        log.info("결제 데이터 초기화 완료: {} 개", paymentRepository.count());
//    }
//
//    /**
//     * 결제 생성 헬퍼 메서드
//     * @param order 주문
//     * @param paymentNumber 결제 번호
//     * @param method 결제 방법
//     * @param status 결제 상태
//     * @param createdAt 생성 시간
//     */
//    private void createPayment(Order order, String paymentNumber, PaymentMethod method, PaymentStatus status, LocalDateTime createdAt) {
//        // 결제 생성
//        Payment payment = Payment.builder()
//                .paymentNumber(paymentNumber)
//                .order(order)
//                .amount(order.getTotalAmount())
//                .method(method)
//                .status(status)
//                .build();
//
//        // 결제 성공인 경우 결제 시간 설정
//        if (status == PaymentStatus.COMPLETED) {
//            payment.setPaymentTime(createdAt);
//        }
//
//        // 결제 저장
//        paymentRepository.save(payment);
//    }
}
