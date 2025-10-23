package com.mock.interview.service.interview.impl;

import com.mock.interview.lib.configuration.AppProperties;
import com.mock.interview.lib.contract.CommonNotificationService;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.NotificationModel;
import com.mock.interview.lib.model.NotificationType;
import com.mock.interview.lib.util.JsonHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service("notificationService")
@RequiredArgsConstructor
public class NotificationServiceImpl implements CommonNotificationService<InterviewModel> {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AppProperties appProperties;

    @Override
    @Async("taskExecutor")
    @Transactional(transactionManager = "kafkaTransactionManager")
    public void send(InterviewModel interview) {
        try {
            var notification = NotificationModel.builder()
                    .topic("notifications")
                    .rule("interview")
                    .sendTo("")
                    .type(NotificationType.EMAIL)
                    .content("Interview: '%s' in status: '%s'".formatted(interview.getId(), interview.getStatus()))
                    .build();
            CompletableFuture.runAsync(
                    () -> kafkaTemplate.send(appProperties.getKafkaTopicName(), JsonHelper.toJson(notification))
            );
        } catch (Exception e) {
            log.warn("Failed to send completion notification: {}", e.getMessage());
            CompletableFuture.failedFuture(e);
        }
    }
}
