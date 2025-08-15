package com.mock.interview.notification.service;

import com.mock.interview.lib.model.NotificationModel;

public interface SmsService {

    void send(NotificationModel notification);
}
