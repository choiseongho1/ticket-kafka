package com.study.ticket.global.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 카프카 컨슈머 설정 클래스
 */
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    /**
     * 카프카 컨슈머 설정을 생성합니다.
     * @return 컨슈머 설정
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        // 자동 커밋 비활성화 (수동 커밋 사용)
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 오프셋 리셋 전략
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // 한 번에 가져올 최대 레코드 수
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        // 트랜잭션 격리 수준
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        // JsonDeserializer 신뢰할 수 있는 패키지
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.study.ticket.*");
        return props;
    }

    /**
     * 카프카 컨슈머 팩토리를 생성합니다.
     * @return 컨슈머 팩토리
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * 카프카 리스너 컨테이너 팩토리를 생성합니다.
     * @return 리스너 컨테이너 팩토리
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // 배치 리스닝 활성화
        factory.setBatchListener(true);
        // 동시성 설정
        factory.setConcurrency(3);
        // 수동 커밋 설정
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }
}
