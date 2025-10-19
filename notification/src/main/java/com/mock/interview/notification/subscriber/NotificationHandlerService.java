package com.mock.interview.notification.subscriber;

import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.model.NotificationModel;
import com.mock.interview.lib.util.AsyncHelper;
import com.mock.interview.lib.util.JsonHelper;
import com.mock.interview.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationHandlerService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private final List<NotificationService> services;

    public void processMassage(String message) {
        var dto = (NotificationModel) JsonHelper.fromJson(message, NotificationModel.class);

        services.stream()
                .filter(s -> s.getNotificationType().equals(dto.getType())
                )
                .findFirst()
                .ifPresentOrElse(service ->
                                executorService.submit(() -> service.send(dto)),
                        () -> {
                            var er = "Unknown notification type %s".formatted(dto.getType());
                            log.error(er, message);
                            throw new MockInterviewException(er, 400);
                        });
    }
}
