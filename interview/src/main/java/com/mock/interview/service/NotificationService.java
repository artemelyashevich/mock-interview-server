package com.mock.interview.service;

import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.NotificationModel;

public interface NotificationService {

    void send(NotificationModel notificationModel);

    void sendNotificationAsync(InterviewModel interview);
}
