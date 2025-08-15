package com.mock.interview.notification.subscriber;

import com.mock.interview.lib.model.NotificationModel;
import com.mock.interview.lib.model.NotificationType;
import com.mock.interview.lib.util.JsonHelper;
import com.mock.interview.notification.service.EmailService;
import com.mock.interview.notification.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationHandlerService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private final EmailService emailService;
    private final SmsService smsService;

    public void processMassage(String message) {
        executorService.submit(() -> {
            try {
                process(message);
            }
            catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private void process(String message) {
        var dto = (NotificationModel) JsonHelper.fromJson(message, NotificationModel.class);

        switch (dto.getType()) {
            case EMAIL -> emailService.send(dto);
            case SMS -> smsService.send(dto);
            default -> log.error(dto.toString());
        }
    }
}
