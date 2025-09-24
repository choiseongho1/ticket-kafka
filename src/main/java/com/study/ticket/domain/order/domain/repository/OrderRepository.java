package com.study.ticket.domain.order.domain.repository;

import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.order.domain.enums.OrderStatus;
import com.study.ticket.domain.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 주문 리포지토리 인터페이스
 */
public interface OrderRepository extends JpaRepository<Order, Long>, OrderDslRepository {

    /**
     * 주문 번호로 주문을 조회합니다.
     * @param orderNumber 주문 번호
     * @return 주문 (Optional)
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * 사용자의 주문 목록을 조회합니다.
     * @param user 사용자
     * @param pageable 페이지 정보
     * @return 주문 페이지
     */
    Page<Order> findByUser(User user, Pageable pageable);

    /**
     * 사용자의 특정 상태의 주문 목록을 조회합니다.
     * @param user 사용자
     * @param status 주문 상태
     * @param pageable 페이지 정보
     * @return 주문 페이지
     */
    Page<Order> findByUserAndStatus(User user, OrderStatus status, Pageable pageable);

    /**
     * 결제 마감 시간이 지난 결제 대기 중인 주문 목록을 조회합니다.
     * @param now 현재 시간
     * @return 주문 목록
     */
    @Query("SELECT o FROM Order o WHERE o.status = 'PAYMENT_PENDING' AND o.paymentDeadline < :now")
    List<Order> findExpiredPaymentPendingOrders(@Param("now") LocalDateTime now);

    /**
     * 주문을 비관적 락으로 조회합니다.
     * @param id 주문 ID
     * @return 주문 (Optional)
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdWithPessimisticLock(@Param("id") Long id);

    /**
     * 주문을 낙관적 락으로 조회합니다.
     * @param id 주문 ID
     * @return 주문 (Optional)
     */
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdWithOptimisticLock(@Param("id") Long id);
}
