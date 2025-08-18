package com.mock.interview.auth.application.port.in;

import com.mock.interview.lib.model.NotificationModel;

public interface NotificationService {

    void send(NotificationModel notificationModel);
}
