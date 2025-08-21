package com.mock.interview.notification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.RetryListener;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.HashMap;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, Object> producerFactory(
            @Value("${app.kafka.transaction-id-prefix}") String transactionIdPrefix,
            ObjectMapper objectMapper
    ) {
        var configProperties = new HashMap<String, Object>();

        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProperties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProperties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionIdPrefix + "-1");

        var serializer = new JsonSerializer<Object>(objectMapper);
        serializer.setAddTypeInfo(false);

        var factory = new DefaultKafkaProducerFactory<>(
                configProperties,
                new StringSerializer(),
                serializer
        );

        factory.setTransactionIdPrefix(transactionIdPrefix);

        return factory;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTransactionManager<String, Object> kafkaTransactionManager(
            ProducerFactory<String, Object> producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            KafkaTemplate<String, String> kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(4);

        factory.setCommonErrorHandler(createErrorHandler(kafkaTemplate));

        return factory;
    }

    private DefaultErrorHandler createErrorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        ExponentialBackOff backOff = new ExponentialBackOff(1000L, 2.0);
        backOff.setMaxAttempts(3);
        backOff.setMaxElapsedTime(10000L);

        return new DefaultErrorHandler((record, exception) -> {
            log.warn("All retries exhausted for record: {}", record.value());
        }, backOff);
    }

    @Bean
    public ConsumerFactory<String, Object> orderEventConsumerFactory() {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public DeadLetterPublishingRecoverer dlqRecoverer(
            KafkaOperations<String, Object> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) ->
                        new TopicPartition(record.topic() + "-dlt", record.partition())
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            DeadLetterPublishingRecoverer dlqRecoverer
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(orderEventConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        factory.setBatchListener(true);
        var errorHandler = new DefaultErrorHandler(dlqRecoverer, createBackOff());
        errorHandler.setRetryListeners(new CustomRetryListener());

        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }


    private BackOff createBackOff() {
        var backOff = new ExponentialBackOff(1000L, 2.0);
        backOff.setMaxElapsedTime(15000L);
        return backOff;
    }

    static class CustomRetryListener implements RetryListener {
        @Override
        public void failedDelivery(ConsumerRecord<?, ?> record, Exception ex, int deliveryAttempt) {
            log.warn("Failed delivery attempt {} for message: {}",
                    deliveryAttempt, record.value(), ex);
        }

        @Override
        public void recovered(ConsumerRecord<?, ?> record, Exception ex) {
            log.info("Message recovered to DLT: {}", record.value());
        }
    }
}
