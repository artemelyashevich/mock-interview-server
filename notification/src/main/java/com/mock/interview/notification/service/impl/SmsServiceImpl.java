package com.mock.interview.notification.service.impl;

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

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @Override
    public void send(NotificationModel notification) {
        Twilio.init(accountSid, authToken);

        Message.creator(
                new PhoneNumber(notification.getSendTo()),
                new PhoneNumber(fromPhoneNumber),
                notification.getContent())
            .create();

        log.info("SMS SENT");
    }
}
