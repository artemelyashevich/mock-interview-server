package com.mock.interview.notification.service.impl;

import com.mock.interview.lib.configuration.AppProperties;
import com.mock.interview.lib.model.NotificationModel;
import com.mock.interview.lib.model.NotificationType;
import com.mock.interview.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements NotificationService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    private final AppProperties appProperties;

    @Override
    public void send(NotificationModel notificationModel) {
        log.debug("Attempting to send email.");

        try {
            var mimeMessage = this.javaMailSender.createMimeMessage();
            var helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
            );

            var context = new Context();
            context.setVariables(Map.of(
                "text", notificationModel.getContent()
            ));

            helper.setFrom(appProperties.getEmail().getSender());
            helper.setTo(notificationModel.getSendTo());
            helper.setSubject(notificationModel.getRule());

            var template = this.templateEngine.process(notificationModel.getRule(), context);

            helper.setText(template, true);

            this.javaMailSender.send(mimeMessage);

            log.info("Email has been sent.");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.EMAIL;
    }
}
