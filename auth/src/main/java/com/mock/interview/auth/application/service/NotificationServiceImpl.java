package com.mock.interview.auth.application.service;

import com.mock.interview.auth.application.port.in.NotificationService;
import com.mock.interview.lib.configuration.AppProperties;
import com.mock.interview.lib.model.NotificationModel;
import com.mock.interview.lib.util.JsonHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final AppProperties appProperties;

    @Override
    @Transactional(transactionManager = "kafkaTransactionManager")
    public void send(NotificationModel notificationModel) {
            kafkaTemplate.send(appProperties.getKafkaTopicName(), JsonHelper.toJson(notificationModel));
    }
}
