package com.mock.interview.notification.service;

import com.mock.interview.lib.model.NotificationModel;
import com.mock.interview.lib.model.NotificationType;

public interface NotificationService {

    void send(NotificationModel notification);

    NotificationType getNotificationType();
}
