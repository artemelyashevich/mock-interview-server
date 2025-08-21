package com.mock.interview.notification.subscriber;

import com.mock.interview.lib.util.JsonHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSubscriber {

    private final NotificationHandlerService notificationHandlerService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(
            topics = "notifications",
            concurrency = "4",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional("kafkaTransactionManager")
    public void listen(List<ConsumerRecord<String, String>> records) {
        log.debug("Received batch of {} records", records.size());

        var successCount = new AtomicInteger();
        var failureCount = new AtomicInteger();

        records.forEach(record -> {
            try {
                processSingleRecord(record);
                successCount.incrementAndGet();
            } catch (Exception e) {
                failureCount.incrementAndGet();
                handleProcessingFailure(record, e);
            }
        });

        if (failureCount.get() > 0) {
            log.warn("Batch processing completed. Success: {}, Failed: {}",
                    successCount.get(), failureCount.get());
        } else {
            log.debug("Batch processing completed successfully. Processed: {} records",
                    successCount.get());
        }
    }

    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            listeners = "retryListener"
    )
    protected void processSingleRecord(ConsumerRecord<String, String> record) {
        var message = record.value();
        log.debug("Processing record: key={}, offset={}, value={}",
                record.key(), record.offset(), message);

        notificationHandlerService.processMassage(message);
    }

    private void handleProcessingFailure(ConsumerRecord<String, String> record, Exception exception) {
        log.error("Failed to process notification after retries: key={}, value={}",
                record.key(), record.value(), exception);

        sendToDeadLetterTopic(record, exception);
    }

    private void sendToDeadLetterTopic(ConsumerRecord<String, String> record, Exception exception) {
        try {
            var dltMessage = MessageBuilder
                    .withPayload(record.value())
                    .setHeader(KafkaHeaders.ORIGINAL_TOPIC, record.topic())
                    .setHeader(KafkaHeaders.ORIGINAL_PARTITION, record.partition())
                    .setHeader(KafkaHeaders.ORIGINAL_OFFSET, record.offset())
                    .setHeader(KafkaHeaders.ORIGINAL_TIMESTAMP, record.timestamp())
                    .setHeader(KafkaHeaders.EXCEPTION_MESSAGE, exception.getMessage())
                    .setHeader(KafkaHeaders.KEY, record.key())
                    .setHeader("x-retry-attempts", "exhausted")
                    .build();

            kafkaTemplate.send("notifications-dlt", JsonHelper.toJson(dltMessage));
            log.debug("Sent message to DLT: key={}", record.key());

        } catch (Exception dltException) {
            log.error("Failed to send message to DLT: key={}, value={}",
                    record.key(), record.value(), dltException);
        }
    }
}