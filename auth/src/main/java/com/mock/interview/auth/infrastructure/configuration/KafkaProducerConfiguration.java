package com.mock.interview.auth.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mock.interview.lib.configuration.AppProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfiguration {

    private final AppProperties appProperties;

    @Bean
    public ProducerFactory<String, Object> producerFactory(
            ObjectMapper objectMapper
    ) {
       var configProperties = new HashMap<String, Object>();

        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProperties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProperties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, appProperties.getKafka().getTransactionIdPrefix() + "-1");

        var serializer = new JsonSerializer<Object>(objectMapper);
        serializer.setAddTypeInfo(false);

        var factory = new DefaultKafkaProducerFactory<>(
                configProperties,
                new StringSerializer(),
                serializer
        );

        factory.setTransactionIdPrefix(appProperties.getKafka().getTransactionIdPrefix());

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
}
