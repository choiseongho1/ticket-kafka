package com.study.ticket.domain.payment.domain.repository;

import com.study.ticket.domain.order.domain.entity.Order;
import com.study.ticket.domain.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 결제 리포지토리 인터페이스
 */
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentDslRepository {

    /**
     * 주문에 해당하는 결제를 조회합니다.
     * @param order 주문
     * @return 결제 (Optional)
     */
    Optional<Payment> findByOrder(Order order);

    /**
     * 주문에 해당하는 결제가 존재하는지 확인합니다.
     * @param order 주문
     * @return 존재 여부
     */
    boolean existsByOrder(Order order);
}
