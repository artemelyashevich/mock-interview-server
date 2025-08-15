package com.mock.interview.notification.service;

import com.mock.interview.lib.model.NotificationModel;

public interface EmailService {

    void send(NotificationModel notificationModel);
}
