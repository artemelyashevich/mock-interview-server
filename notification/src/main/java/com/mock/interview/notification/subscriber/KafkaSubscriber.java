package com.mock.interview.notification.subscriber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSubscriber {

    private final NotificationHandlerService notificationHandlerService;

    @KafkaListener(
        topics = "notifications",
        concurrency = "4",
        groupId = "notification-group"
    )
    public void listen(List<ConsumerRecord<String, String>> records) {
        log.info("receive records: {}", records.size());

        records.forEach(record -> {
            try {
                var message = record.value();

                log.debug("Processing record: {}", message);

                notificationHandlerService.processMassage(message);
            } catch (Exception e) {
                log.error("Error processing notification: {}", record.value(), e);
            }
        });
    }
}
