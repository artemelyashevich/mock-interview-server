package com.mock.interview.notification.service.impl;

import com.mock.interview.lib.configuration.AppProperties;
import com.mock.interview.lib.model.NotificationModel;
import com.mock.interview.notification.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

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
}
