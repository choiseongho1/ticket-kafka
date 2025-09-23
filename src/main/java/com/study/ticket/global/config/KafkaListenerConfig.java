package com.study.ticket.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * 카프카 리스너 공통 설정 클래스
 */
@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private final ConsumerFactory<String, Object> consumerFactory;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    /**
     * 카프카 리스너 컨테이너 팩토리를 생성합니다.
     * @return 카프카 리스너 컨테이너 팩토리
     */
    @Bean(name = "errorHandlingKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> errorHandlingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        
        // 배치 리스닝 비활성화 (개별 메시지 처리)
        factory.setBatchListener(false);
        
        // 동시성 설정
        factory.setConcurrency(3);
        
        // 수동 커밋 설정
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        
        // 에러 핸들러 설정
        factory.setCommonErrorHandler(kafkaErrorHandler());
        
        return factory;
    }
    
    /**
     * 카프카 에러 핸들러를 생성합니다.
     * @return 카프카 에러 핸들러
     */
    @Bean
    public CommonErrorHandler kafkaErrorHandler() {
        // DLQ(Dead Letter Queue)로 실패한 메시지를 전송하는 리커버러
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, exception) -> {
                    // 원본 토픽 이름에 .DLT 접미사를 붙여 DLQ 토픽 이름 생성
                    String originalTopic = record.topic();
                    String dltTopic = originalTopic + ".DLT";
                    return new org.apache.kafka.common.TopicPartition(dltTopic, record.partition());
                });
        
        // 에러 핸들러 설정
        // 최대 3번 재시도, 재시도 간격 5초
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(5000L, 3));
        
        // 특정 예외는 재시도하지 않음
        errorHandler.addNotRetryableExceptions(
                IllegalArgumentException.class,
                com.fasterxml.jackson.core.JsonParseException.class
        );
        
        return errorHandler;
    }
}
