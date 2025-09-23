package com.study.ticket.global.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * 카프카 토픽 설정 클래스
 */
@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topics.order-events}")
    private String orderEventsTopic;

    @Value("${kafka.topics.payment-events}")
    private String paymentEventsTopic;

    @Value("${kafka.topics.ticket-events}")
    private String ticketEventsTopic;

    @Value("${kafka.topics.movie-events}")
    private String movieEventsTopic;

    @Value("${kafka.topics.screening-events}")
    private String screeningEventsTopic;

    /**
     * 카프카 관리자 설정을 생성합니다.
     * @return 카프카 관리자
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    /**
     * 주문 이벤트 토픽을 생성합니다.
     * @return 주문 이벤트 토픽
     */
    @Bean
    public NewTopic orderEventsTopic() {
        // 파티션 수 3, 복제 팩터 1 (개발 환경)
        return new NewTopic(orderEventsTopic, 3, (short) 1);
    }

    /**
     * 결제 이벤트 토픽을 생성합니다.
     * @return 결제 이벤트 토픽
     */
    @Bean
    public NewTopic paymentEventsTopic() {
        return new NewTopic(paymentEventsTopic, 3, (short) 1);
    }

    /**
     * 티켓 이벤트 토픽을 생성합니다.
     * @return 티켓 이벤트 토픽
     */
    @Bean
    public NewTopic ticketEventsTopic() {
        return new NewTopic(ticketEventsTopic, 3, (short) 1);
    }

    /**
     * 영화 이벤트 토픽을 생성합니다.
     * @return 영화 이벤트 토픽
     */
    @Bean
    public NewTopic movieEventsTopic() {
        return new NewTopic(movieEventsTopic, 3, (short) 1);
    }

    /**
     * 상영 이벤트 토픽을 생성합니다.
     * @return 상영 이벤트 토픽
     */
    @Bean
    public NewTopic screeningEventsTopic() {
        return new NewTopic(screeningEventsTopic, 3, (short) 1);
    }
}
