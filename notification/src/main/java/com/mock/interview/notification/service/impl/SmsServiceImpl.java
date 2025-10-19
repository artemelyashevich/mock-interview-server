package com.mock.interview.notification.service.impl;

import com.mock.interview.lib.configuration.AppProperties;
import com.mock.interview.lib.model.NotificationModel;
import com.mock.interview.lib.model.NotificationType;
import com.mock.interview.notification.service.NotificationService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements NotificationService {

    private final AppProperties appProperties;

    @Override
    public void send(NotificationModel notification) {
        Twilio.init(appProperties.getTwilio().getCount().getSid(), appProperties.getTwilio().getAuth().getToken());

        Message.creator(
                new PhoneNumber(notification.getSendTo()),
                new PhoneNumber(appProperties.getTwilioPhoneNumber()),
                notification.getContent())
            .create();

        log.info("SMS SENT");
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.SMS;
    }
}
