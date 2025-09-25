package com.study.ticket.global.config;

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
import com.study.ticket.domain.ticket.domain.entity.MovieTicket;
import com.study.ticket.domain.ticket.domain.enums.TicketStatus;
import com.study.ticket.domain.ticket.domain.repository.MovieTicketRepository;
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
import java.util.UUID;

/**
 * 애플리케이션 시작 시 기본 데이터를 초기화하는 클래스
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInit {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final MovieTicketRepository movieTicketRepository;

    /**
     * 개발 환경에서만 실행되는 데이터 초기화 메서드
     */
    @Bean
    @Profile("!prod")
    public CommandLineRunner initData() {
        return args -> {
            log.info("데이터 초기화 시작");
            
            // 기존 데이터 삭제
            movieTicketRepository.deleteAll();
            paymentRepository.deleteAll();
            orderRepository.deleteAll();
            screeningRepository.deleteAll();
            movieRepository.deleteAll();
            userRepository.deleteAll();
            
            // 사용자 데이터 생성
            User user1 = createUser("user1@example.com", "password1", "홍길동", "010-1234-5678", "USER", true);
            User user2 = createUser("user2@example.com", "password2", "김철수", "010-2345-6789", "USER", true);
            User admin = createUser("admin@example.com", "admin123", "관리자", "010-9876-5432", "ADMIN", true);
            
            // 영화 데이터 생성
            Movie movie1 = createMovie("어벤져스: 엔드게임", "인피니티 워 이후 절반만 살아남은 지구, 마지막 희망이 된 어벤져스",
                    "앤서니 루소, 조 루소", "로버트 다우니 주니어, 크리스 에반스, 크리스 헴스워스", 181,
                    LocalDate.of(2025, 9, 20), LocalDate.of(2025, 11, 20), Genre.ACTION, Rating.TWELVE);
            
            Movie movie2 = createMovie("미션 임파서블: 데드 레코닝", "가장 위험한 작전, 모든 것을 건 미션이 시작된다",
                    "크리스토퍼 맥쿼리", "톰 크루즈, 헤일리 앳웰, 사이먼 페그", 163,
                    LocalDate.of(2025, 9, 15), LocalDate.of(2025, 11, 15), Genre.ACTION, Rating.FIFTEEN);
            
            Movie movie3 = createMovie("토이 스토리 5", "장난감들의 새로운 모험",
                    "앤드류 스탠튼", "톰 행크스, 팀 알렌", 110,
                    LocalDate.of(2025, 9, 10), LocalDate.of(2025, 11, 10), Genre.ANIMATION, Rating.ALL);
            
            Movie movie4 = createMovie("인사이드 아웃 2", "감정들이 다시 한번 모험을 떠납니다",
                    "피트 닥터", "에이미 포엘러, 필리스 스미스", 105,
                    LocalDate.of(2025, 9, 5), LocalDate.of(2025, 11, 5), Genre.ANIMATION, Rating.ALL);
            
            Movie movie5 = createMovie("조커: 폴리 아 두", "광기와 혼돈의 세계로 다시 돌아온 조커",
                    "토드 필립스", "호아킨 피닉스, 레이디 가가", 142,
                    LocalDate.of(2025, 9, 25), LocalDate.of(2025, 11, 25), Genre.THRILLER, Rating.ADULT);
            
            // 상영 데이터 생성
            LocalDateTime now = LocalDateTime.now();
            
            Screening screening1 = createScreening(movie1, "1관", 
                    now.plusDays(1).withHour(10).withMinute(0).withSecond(0),
                    now.plusDays(1).withHour(13).withMinute(1).withSecond(0),
                    100, 100, 12000);
            
            Screening screening2 = createScreening(movie1, "1관", 
                    now.plusDays(1).withHour(14).withMinute(0).withSecond(0),
                    now.plusDays(1).withHour(17).withMinute(1).withSecond(0),
                    100, 100, 14000);
            
            Screening screening3 = createScreening(movie1, "2관", 
                    now.plusDays(1).withHour(18).withMinute(0).withSecond(0),
                    now.plusDays(1).withHour(21).withMinute(1).withSecond(0),
                    80, 80, 16000);
            
            Screening screening4 = createScreening(movie2, "3관", 
                    now.plusDays(1).withHour(11).withMinute(0).withSecond(0),
                    now.plusDays(1).withHour(13).withMinute(43).withSecond(0),
                    120, 120, 12000);
            
            Screening screening5 = createScreening(movie2, "3관", 
                    now.plusDays(1).withHour(15).withMinute(0).withSecond(0),
                    now.plusDays(1).withHour(17).withMinute(43).withSecond(0),
                    120, 120, 14000);
            
            Screening screening6 = createScreening(movie3, "4관", 
                    now.plusDays(1).withHour(10).withMinute(30).withSecond(0),
                    now.plusDays(1).withHour(12).withMinute(20).withSecond(0),
                    80, 80, 10000);
            
            Screening screening7 = createScreening(movie3, "4관", 
                    now.plusDays(1).withHour(13).withMinute(30).withSecond(0),
                    now.plusDays(1).withHour(15).withMinute(20).withSecond(0),
                    80, 80, 12000);
            
            Screening screening8 = createScreening(movie4, "5관", 
                    now.plusDays(1).withHour(11).withMinute(30).withSecond(0),
                    now.plusDays(1).withHour(13).withMinute(15).withSecond(0),
                    60, 60, 10000);
            
            Screening screening9 = createScreening(movie5, "IMAX관", 
                    now.plusDays(1).withHour(19).withMinute(0).withSecond(0),
                    now.plusDays(1).withHour(21).withMinute(22).withSecond(0),
                    150, 150, 18000);
            
            Screening screening10 = createScreening(movie5, "IMAX관", 
                    now.plusDays(1).withHour(22).withMinute(0).withSecond(0),
                    now.plusDays(2).withHour(0).withMinute(22).withSecond(0),
                    150, 150, 16000);
            
            // 주문 데이터 생성
            Order order1 = createOrder("ORD-20250925-001", user1, screening1, 2, 24000, OrderStatus.PAYMENT_COMPLETED, 
                    now.plusMinutes(30));
            OrderItem orderItem1 = createOrderItem(order1, "A1", 12000);
            OrderItem orderItem2 = createOrderItem(order1, "A2", 12000);
            
            Order order2 = createOrder("ORD-20250925-002", user2, screening3, 3, 48000, OrderStatus.PAYMENT_COMPLETED, 
                    now.plusMinutes(30));
            OrderItem orderItem3 = createOrderItem(order2, "B5", 16000);
            OrderItem orderItem4 = createOrderItem(order2, "B6", 16000);
            OrderItem orderItem5 = createOrderItem(order2, "B7", 16000);
            
            Order order3 = createOrder("ORD-20250925-003", user1, screening6, 2, 20000, OrderStatus.CREATED, 
                    now.plusMinutes(30));
            OrderItem orderItem6 = createOrderItem(order3, "C3", 10000);
            OrderItem orderItem7 = createOrderItem(order3, "C4", 10000);
            
            // 결제 데이터 생성
            Payment payment1 = createPayment("PAY-20250925-001", order1, 24000, PaymentMethod.CREDIT_CARD, 
                    PaymentStatus.COMPLETED, "payment_key_123456", now.minusMinutes(15));
            
            Payment payment2 = createPayment("PAY-20250925-002", order2, 48000, PaymentMethod.MOBILE_PAYMENT, 
                    PaymentStatus.COMPLETED, "payment_key_789012", now.minusMinutes(15));
            
            Payment payment3 = createPayment("PAY-20250925-003", order3, 20000, PaymentMethod.CREDIT_CARD, 
                    PaymentStatus.PENDING, null, null);
            
            // 티켓 데이터 생성
            MovieTicket ticket1 = createTicket("TKT-20250925-001-A1", order1, user1, screening1, "A1", 
                    TicketStatus.ISSUED, now.minusMinutes(14), null, null, "qr_code_a1_123456");
            
            MovieTicket ticket2 = createTicket("TKT-20250925-001-A2", order1, user1, screening1, "A2", 
                    TicketStatus.ISSUED, now.minusMinutes(14), null, null, "qr_code_a2_123456");
            
            MovieTicket ticket3 = createTicket("TKT-20250925-002-B5", order2, user2, screening3, "B5", 
                    TicketStatus.ISSUED, now.minusMinutes(14), null, null, "qr_code_b5_789012");
            
            MovieTicket ticket4 = createTicket("TKT-20250925-002-B6", order2, user2, screening3, "B6", 
                    TicketStatus.ISSUED, now.minusMinutes(14), null, null, "qr_code_b6_789012");
            
            MovieTicket ticket5 = createTicket("TKT-20250925-002-B7", order2, user2, screening3, "B7", 
                    TicketStatus.USED, now.minusMinutes(14), now.minusMinutes(5), null, "qr_code_b7_789012");
            
            log.info("데이터 초기화 완료");
        };
    }
    
    private User createUser(String email, String password, String name, String phone, String role, boolean enabled) {
        User user = User.builder()
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .role(role)
                .enabled(enabled)
                .build();
        return userRepository.save(user);
    }
    
    private Movie createMovie(String title, String description, String director, String actors, Integer runningTime,
                             LocalDate releaseDate, LocalDate endDate, Genre genre, Rating rating) {
        Movie movie = Movie.builder()
                .title(title)
                .description(description)
                .director(director)
                .actors(actors)
                .runningTime(runningTime)
                .releaseDate(releaseDate)
                .endDate(endDate)
                .genre(genre)
                .rating(rating)
                .build();
        return movieRepository.save(movie);
    }
    
    private Screening createScreening(Movie movie, String screenName, LocalDateTime startTime, LocalDateTime endTime,
                                     Integer totalSeats, Integer availableSeats, Integer price) {
        Screening screening = Screening.builder()
                .movie(movie)
                .screenName(screenName)
                .startTime(startTime)
                .endTime(endTime)
                .totalSeats(totalSeats)
                .availableSeats(availableSeats)
                .price(price)
                .build();
        return screeningRepository.save(screening);
    }
    
    private Order createOrder(String orderNumber, User user, Screening screening, Integer seatCount, Integer totalAmount,
                             OrderStatus status, LocalDateTime paymentDeadline) {
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .user(user)
                .screening(screening)
                .seatCount(seatCount)
                .totalAmount(totalAmount)
                .status(status)
                .paymentDeadline(paymentDeadline)
                .build();
        return orderRepository.save(order);
    }
    
    private OrderItem createOrderItem(Order order, String seatNumber, Integer price) {
        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .seatNumber(seatNumber)
                .price(price)
                .build();
        order.addOrderItem(orderItem);
        return orderItem;
    }
    
    private Payment createPayment(String paymentNumber, Order order, Integer amount, PaymentMethod method,
                                 PaymentStatus status, String paymentKey, LocalDateTime paymentTime) {
        Payment payment = Payment.builder()
                .paymentNumber(paymentNumber)
                .order(order)
                .amount(amount)
                .method(method)
                .status(status)
                .paymentKey(paymentKey)
                .paymentTime(paymentTime)
                .build();
        return paymentRepository.save(payment);
    }
    
    private MovieTicket createTicket(String ticketNumber, Order order, User user, Screening screening, String seatNumber,
                                    TicketStatus status, LocalDateTime issueTime, LocalDateTime usedTime,
                                    LocalDateTime cancellationTime, String qrCode) {
        MovieTicket ticket = MovieTicket.builder()
                .ticketNumber(ticketNumber)
                .order(order)
                .user(user)
                .screening(screening)
                .seatNumber(seatNumber)
                .status(status)
                .issueTime(issueTime)
                .usedTime(usedTime)
                .cancellationTime(cancellationTime)
                .qrCode(qrCode)
                .build();
        return movieTicketRepository.save(ticket);
    }
}
