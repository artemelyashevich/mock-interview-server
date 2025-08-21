package com.elyashevich.interview.application.port.in;

import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.NotificationModel;

public interface NotificationService {

    void send(NotificationModel notificationModel);

    void sendNotificationAsync(InterviewModel interview);
}
