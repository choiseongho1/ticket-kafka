package com.study.ticket.global.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 카프카 프로듀서 설정 클래스
 */
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * 카프카 프로듀서 설정을 생성합니다.
     * @return 프로듀서 설정
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // 멱등성 설정
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        // 모든 복제본이 메시지를 받았는지 확인
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        // 재시도 횟수
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 배치 크기
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 압축 타입
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        // 버퍼 메모리
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        return props;
    }

    /**
     * 카프카 프로듀서 팩토리를 생성합니다.
     * @return 프로듀서 팩토리
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * 카프카 템플릿을 생성합니다.
     * @return 카프카 템플릿
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
