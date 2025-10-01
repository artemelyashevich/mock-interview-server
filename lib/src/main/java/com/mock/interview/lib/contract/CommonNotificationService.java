package com.mock.interview.lib.contract;

import com.mock.interview.lib.model.InterviewModel;

public interface CommonNotificationService<T> {

    public void send(T t);
}
