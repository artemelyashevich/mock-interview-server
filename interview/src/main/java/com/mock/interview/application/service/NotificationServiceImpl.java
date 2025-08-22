package com.mock.interview.application.service;

import com.elyashevich.interview.application.port.in.NotificationService;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.NotificationModel;
import com.mock.interview.lib.model.NotificationType;
import com.mock.interview.lib.util.JsonHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service("notificationService")
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final BeanFactory beanFactory;

    @Value("${app.kafka.topic.name}")
    private String topicName;

    @Override
    @Transactional(transactionManager = "kafkaTransactionManager")
    public void send(NotificationModel notificationModel) {
        var result = kafkaTemplate.send(topicName, JsonHelper.toJson(notificationModel));
        result
                .completeAsync((r,v)->{
                    return r;
                })
                .completeExceptionally();
    }

    // FIXME
    @Async("taskExecutor")
    public void sendNotificationAsync(InterviewModel interview) {
        try {
            var notification = NotificationModel.builder()
                    .topic("notifications")
                    .rule("interview")
                    .sendTo("")
                    .type(NotificationType.EMAIL)
                    .content("Interview: '%s' in status: '%s'".formatted(interview.getId(), interview.getStatus()))
                    .build();
            ((NotificationServiceImpl) beanFactory.getBean("notificationService")).send(notification);
            CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.warn("Failed to send completion notification: {}", e.getMessage());
            CompletableFuture.failedFuture(e);
        }
    }
}
