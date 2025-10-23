package com.mock.interview.auth2.service;

import com.mock.interview.auth2.configuration.AppProps;
import com.mock.interview.lib.contract.CommonNotificationService;
import com.mock.interview.lib.model.NotificationModel;
import com.mock.interview.lib.model.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService implements CommonNotificationService<NotificationModel> {

    private final AppProps appProperties;

    @Override
    public void send(NotificationModel model) {
        var notification = NotificationModel.builder()
                .topic("notifications")
                .rule("otp")
                .sendTo("")
                .type(NotificationType.EMAIL)
                .content("")
                .build();
        // CompletableFuture.runAsync(() -> kafkaTemplate.send(appProperties.getKafkaTopicName(), JsonHelper.toJson(notification))
        // );
    }
}
