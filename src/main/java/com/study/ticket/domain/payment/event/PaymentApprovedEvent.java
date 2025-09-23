package com.study.ticket.domain.payment.event;

import com.study.ticket.domain.payment.domain.entity.Payment;
import com.study.ticket.global.event.BaseEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 결제 승인 이벤트
 */
@Getter
@NoArgsConstructor
public class PaymentApprovedEvent extends BaseEvent {

    private static final String EVENT_TYPE = "PAYMENT_APPROVED";
    private static final String AGGREGATE_TYPE = "PAYMENT";

    /**
     * 결제 ID
     */
    private Long paymentId;

    /**
     * 결제 번호
     */
    private String paymentNumber;

    /**
     * 주문 ID
     */
    private Long orderId;

    /**
     * 사용자 ID
     */
    private Long userId;

    /**
     * 결제 금액
     */
    private Integer amount;

    /**
     * 결제 방법
     */
    private String method;

    /**
     * 외부 결제 시스템 키
     */
    private String paymentKey;

    /**
     * 결제 시간
     */
    private LocalDateTime paymentTime;

    /**
     * 결제 승인 이벤트 생성자
     * @param payment 결제 엔티티
     */
    public PaymentApprovedEvent(Payment payment) {
        super(EVENT_TYPE);
        this.paymentId = payment.getId();
        this.paymentNumber = payment.getPaymentNumber();
        this.orderId = payment.getOrder().getId();
        this.userId = payment.getOrder().getUser().getId();
        this.amount = payment.getAmount();
        this.method = payment.getMethod().name();
        this.paymentKey = payment.getPaymentKey();
        this.paymentTime = payment.getPaymentTime();
        
        // 이벤트 키 생성
        setEventKey(generateEventKey(AGGREGATE_TYPE, payment.getId().toString()));
    }
}
