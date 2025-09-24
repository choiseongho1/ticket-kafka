package com.study.ticket.domain.payment.controller;

import com.study.ticket.domain.payment.controller.dto.PaymentApproveRequest;
import com.study.ticket.domain.payment.controller.dto.PaymentCancelRequest;
import com.study.ticket.domain.payment.controller.dto.PaymentCreateRequest;
import com.study.ticket.domain.payment.controller.dto.PaymentResponse;
import com.study.ticket.domain.payment.domain.entity.Payment;
import com.study.ticket.domain.payment.service.PaymentService;
import com.study.ticket.global.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * 결제 컨트롤러
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    // 성공 응답 코드
    private static final String PAYMENT_CREATE_SUCCESS = "PAYMENT_CREATE_SUCCESS";
    private static final String PAYMENT_GET_SUCCESS = "PAYMENT_GET_SUCCESS";
    private static final String PAYMENT_APPROVE_SUCCESS = "PAYMENT_APPROVE_SUCCESS";
    private static final String PAYMENT_CANCEL_SUCCESS = "PAYMENT_CANCEL_SUCCESS";
    
    // 실패 응답 코드
    private static final String PAYMENT_CREATE_FAILED = "PAYMENT_CREATE_FAILED";
    private static final String PAYMENT_GET_FAILED = "PAYMENT_GET_FAILED";
    private static final String PAYMENT_APPROVE_FAILED = "PAYMENT_APPROVE_FAILED";
    private static final String PAYMENT_CANCEL_FAILED = "PAYMENT_CANCEL_FAILED";

    private final PaymentService paymentService;

    /**
     * 결제를 생성합니다.
     * @param request 결제 생성 요청
     * @return 생성된 결제
     */
    @PostMapping
    public ResponseEntity<ResponseDto<PaymentResponse>> createPayment(@RequestBody PaymentCreateRequest request) {
        try {
            log.info("결제 생성 요청: {}", request);
            
            Payment payment = paymentService.createPayment(
                    request.getOrderId(),
                    request.getMethod()
            );
            
            PaymentResponse response = PaymentResponse.from(payment);
            
            ResponseDto<PaymentResponse> responseDto = ResponseDto.<PaymentResponse>builder()
                .responseCode(PAYMENT_CREATE_SUCCESS)
                .responseMessage("결제가 성공적으로 생성되었습니다.")
                .data(response)
                .build();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            log.error("결제 생성 실패: {}", e.getMessage(), e);
            
            ResponseDto<PaymentResponse> errorResponse = ResponseDto.<PaymentResponse>builder()
                .responseCode(PAYMENT_CREATE_FAILED)
                .responseMessage("결제 생성에 실패했습니다: " + e.getMessage())
                .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 결제를 조회합니다.
     * @param paymentId 결제 ID
     * @return 결제
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<ResponseDto<PaymentResponse>> getPayment(@PathVariable Long paymentId) {
        try {
            log.info("결제 조회 요청: {}", paymentId);
            
            Payment payment = paymentService.getPayment(paymentId);
            PaymentResponse response = PaymentResponse.from(payment);
            
            ResponseDto<PaymentResponse> responseDto = ResponseDto.<PaymentResponse>builder()
                .responseCode(PAYMENT_GET_SUCCESS)
                .responseMessage("결제 조회에 성공했습니다.")
                .data(response)
                .build();
            
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("결제 조회 실패: {}", e.getMessage(), e);
            
            ResponseDto<PaymentResponse> errorResponse = ResponseDto.<PaymentResponse>builder()
                .responseCode(PAYMENT_GET_FAILED)
                .responseMessage("결제 조회에 실패했습니다: " + e.getMessage())
                .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 결제를 승인합니다.
     * @param paymentId 결제 ID
     * @param request 결제 승인 요청
     * @return 승인된 결제
     */
    @PostMapping("/{paymentId}/approve")
    public ResponseEntity<ResponseDto<PaymentResponse>> approvePayment(
            @PathVariable Long paymentId,
            @RequestBody PaymentApproveRequest request
    ) {
        try {
            log.info("결제 승인 요청: {}, {}", paymentId, request);
            
            Payment payment = paymentService.approvePayment(
                    paymentId,
                    request.getPaymentKey()
            );
            
            PaymentResponse response = PaymentResponse.from(payment);
            
            ResponseDto<PaymentResponse> responseDto = ResponseDto.<PaymentResponse>builder()
                .responseCode(PAYMENT_APPROVE_SUCCESS)
                .responseMessage("결제 승인에 성공했습니다.")
                .data(response)
                .build();
            
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("결제 승인 실패: {}", e.getMessage(), e);
            
            ResponseDto<PaymentResponse> errorResponse = ResponseDto.<PaymentResponse>builder()
                .responseCode(PAYMENT_APPROVE_FAILED)
                .responseMessage("결제 승인에 실패했습니다: " + e.getMessage())
                .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 결제를 취소합니다.
     * @param paymentId 결제 ID
     * @param request 결제 취소 요청
     * @return 취소된 결제
     */
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<ResponseDto<PaymentResponse>> cancelPayment(
            @PathVariable Long paymentId,
            @RequestBody PaymentCancelRequest request
    ) {
        try {
            log.info("결제 취소 요청: {}, {}", paymentId, request);
            
            Payment payment = paymentService.cancelPayment(
                    paymentId,
                    request.getReason()
            );
            
            PaymentResponse response = PaymentResponse.from(payment);
            
            ResponseDto<PaymentResponse> responseDto = ResponseDto.<PaymentResponse>builder()
                .responseCode(PAYMENT_CANCEL_SUCCESS)
                .responseMessage("결제 취소에 성공했습니다.")
                .data(response)
                .build();
            
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("결제 취소 실패: {}", e.getMessage(), e);
            
            ResponseDto<PaymentResponse> errorResponse = ResponseDto.<PaymentResponse>builder()
                .responseCode(PAYMENT_CANCEL_FAILED)
                .responseMessage("결제 취소에 실패했습니다: " + e.getMessage())
                .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 주문에 대한 결제를 조회합니다.
     * @param orderId 주문 ID
     * @return 결제
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseDto<PaymentResponse>> getPaymentByOrder(@PathVariable Long orderId) {
        try {
            log.info("주문에 대한 결제 조회 요청: {}", orderId);
            
            Payment payment = paymentService.getPaymentByOrder(orderId);
            PaymentResponse response = PaymentResponse.from(payment);
            
            ResponseDto<PaymentResponse> responseDto = ResponseDto.<PaymentResponse>builder()
                .responseCode(PAYMENT_GET_SUCCESS)
                .responseMessage("결제 조회에 성공했습니다.")
                .data(response)
                .build();
            
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("주문에 대한 결제 조회 실패: {}", e.getMessage(), e);
            
            ResponseDto<PaymentResponse> errorResponse = ResponseDto.<PaymentResponse>builder()
                .responseCode(PAYMENT_GET_FAILED)
                .responseMessage("결제 조회에 실패했습니다: " + e.getMessage())
                .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}