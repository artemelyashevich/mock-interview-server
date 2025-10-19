package com.mock.interview.notification.service.impl;

import com.mock.interview.lib.model.NotificationModel;
import com.mock.interview.lib.model.NotificationType;
import com.mock.interview.notification.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class PushServiceImpl implements NotificationService {
    @Override
    public void send(NotificationModel notification) {

    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.PUSH;
    }
}
