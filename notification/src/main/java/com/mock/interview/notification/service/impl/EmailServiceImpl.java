package com.mock.interview.notification.service.impl;

import com.mock.interview.lib.model.NotificationModel;
import com.mock.interview.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Setter
    @Value("${spring.mail.sender.email}")
    private String senderEmail;

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

            helper.setFrom(senderEmail);
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
}
