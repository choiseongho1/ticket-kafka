package com.study.ticket.domain.ticket.domain.repository;

import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.screening.domain.entity.Screening;
import com.study.ticket.domain.ticket.domain.entity.MovieTicket;
import com.study.ticket.domain.ticket.domain.enums.TicketStatus;
import com.study.ticket.domain.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 영화 티켓 리포지토리 인터페이스
 */
public interface MovieTicketRepository extends JpaRepository<MovieTicket, Long> {

    /**
     * 티켓 번호로 티켓을 조회합니다.
     * @param ticketNumber 티켓 번호
     * @return 티켓 (Optional)
     */
    Optional<MovieTicket> findByTicketNumber(String ticketNumber);

    /**
     * 주문에 해당하는 티켓 목록을 조회합니다.
     * @param order 주문
     * @return 티켓 목록
     */
    List<MovieTicket> findByOrder(Order order);

    /**
     * 사용자의 티켓 목록을 조회합니다.
     * @param user 사용자
     * @param pageable 페이지 정보
     * @return 티켓 페이지
     */
    Page<MovieTicket> findByUser(User user, Pageable pageable);
    
    /**
     * 사용자 ID로 티켓 목록을 조회합니다.
     * @param userId 사용자 ID
     * @param pageable 페이지 정보
     * @return 티켓 페이지
     */
    @Query("SELECT t FROM MovieTicket t WHERE t.user.id = :userId")
    Page<MovieTicket> findByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * 사용자의 특정 상태의 티켓 목록을 조회합니다.
     * @param user 사용자
     * @param status 티켓 상태
     * @param pageable 페이지 정보
     * @return 티켓 페이지
     */
    Page<MovieTicket> findByUserAndStatus(User user, TicketStatus status, Pageable pageable);
    
    /**
     * 사용자 ID와 상태로 티켓 목록을 조회합니다.
     * @param userId 사용자 ID
     * @param status 티켓 상태
     * @param pageable 페이지 정보
     * @return 티켓 페이지
     */
    @Query("SELECT t FROM MovieTicket t WHERE t.user.id = :userId AND t.status = :status")
    Page<MovieTicket> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") TicketStatus status, Pageable pageable);

    /**
     * 상영에 해당하는 티켓 목록을 조회합니다.
     * @param screening 상영
     * @return 티켓 목록
     */
    List<MovieTicket> findByScreening(Screening screening);

    /**
     * 상영에 해당하는 특정 상태의 티켓 수를 조회합니다.
     * @param screeningId 상영 ID
     * @param status 티켓 상태
     * @return 티켓 수
     */
    @Query("SELECT COUNT(t) FROM MovieTicket t WHERE t.screening.id = :screeningId AND t.status = :status")
    long countByScreeningIdAndStatus(@Param("screeningId") Long screeningId, @Param("status") TicketStatus status);

    /**
     * 특정 시간 이후에 만료되는 티켓 목록을 조회합니다.
     * @param dateTime 기준 시간
     * @return 티켓 목록
     */
    @Query("SELECT t FROM MovieTicket t JOIN t.screening s WHERE t.status = 'ISSUED' AND s.endTime < :dateTime")
    List<MovieTicket> findExpiredTickets(@Param("dateTime") LocalDateTime dateTime);
}
