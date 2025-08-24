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
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.RetryListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConfig {

    private static final String NOTIFICATION_GROUP_ID = "notification-group";
    private static final long INITIAL_BACKOFF_MS = 1000L;
    private static final double BACKOFF_MULTIPLIER = 2.0;
    private static final long MAX_BACKOFF_MS = 15000L;
    private static final int MAX_RETRY_ATTEMPTS = 3;

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${app.kafka.transaction-id-prefix:notification-txn-}")
    private String transactionIdPrefix;

    @Bean
    public ProducerFactory<String, Object> producerFactory(
            KafkaProperties kafkaProperties,
            ObjectMapper objectMapper) {

        Map<String, Object> configProps = new HashMap<>(kafkaProperties.buildProducerProperties());

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 20);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 32 * 1024); // 32KB

        JsonSerializer<Object> serializer = new JsonSerializer<>(objectMapper);
        serializer.setAddTypeInfo(false);

        DefaultKafkaProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(
                configProps,
                new StringSerializer(),
                serializer
        );

        factory.setTransactionIdPrefix(transactionIdPrefix);
        return factory;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        KafkaTemplate<String, Object> template = new KafkaTemplate<>(producerFactory);
        template.setObservationEnabled(true);
        return template;
    }

    @Bean
    public KafkaTransactionManager<String, Object> kafkaTransactionManager(
            ProducerFactory<String, Object> producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }

    @Bean
    public ConsumerFactory<String, Object> notificationConsumerFactory(
            KafkaProperties kafkaProperties,
            ObjectMapper objectMapper) {

        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, NOTIFICATION_GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "java.lang.Object");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            KafkaTemplate<String, Object> kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(4);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setSyncCommits(true);

        DefaultErrorHandler errorHandler = createErrorHandler(kafkaTemplate);
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }

    private DefaultErrorHandler createErrorHandler(KafkaTemplate<String, Object> kafkaTemplate) {
        ExponentialBackOff backOff = new ExponentialBackOff(INITIAL_BACKOFF_MS, BACKOFF_MULTIPLIER);
        backOff.setMaxElapsedTime(MAX_BACKOFF_MS);

        DeadLetterPublishingRecoverer dlqRecoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) -> new TopicPartition(record.topic() + "-dlt", record.partition())
        );

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(dlqRecoverer, backOff);
        errorHandler.setRetryListeners(new NotificationRetryListener());
        errorHandler.setCommitRecovered(true);
        errorHandler.setAckAfterHandle(false);

        errorHandler.addRetryableExceptions(IllegalStateException.class);
        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);

        return errorHandler;
    }

    @Bean
    public DeadLetterPublishingRecoverer dlqRecoverer(KafkaOperations<String, Object> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> new TopicPartition(record.topic() + "-dlt", record.partition())
        );
    }

    static class NotificationRetryListener implements RetryListener {
        @Override
        public void failedDelivery(ConsumerRecord<?, ?> record, Exception ex, int deliveryAttempt) {
            log.warn("Failed delivery attempt {} for topic {}: {}",
                    deliveryAttempt, record.topic(), record.value(), ex);
        }

        @Override
        public void recovered(ConsumerRecord<?, ?> record, Exception ex) {
            log.info("Message recovered to DLT for topic {}: {}",
                    record.topic(), record.value());
        }

        @Override
        public void recoveryFailed(ConsumerRecord<?, ?> record, Exception original, Exception recovery) {
            log.error("Recovery failed for topic {}: {}",
                    record.topic(), record.value(), recovery);
        }
    }
}