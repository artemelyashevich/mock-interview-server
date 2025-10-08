package com.mock.interview.service;

import com.mock.interview.lib.contract.CommonNotificationService;
import com.mock.interview.lib.dto.CreateReportRequest;
import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewStatus;
import com.mock.interview.lib.model.InterviewTemplateModel;
import com.mock.interview.lib.model.ReportFormat;
import com.mock.interview.lib.util.AsyncHelper;
import com.mock.interview.service.web.ReportWebClientService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

@Profile("prod")
@Slf4j
@Service
@RequiredArgsConstructor
public class OrchestrationService {

    private static final String INTERVIEW_PROCESS_PREFIX = "-------------------- Interview process";

    private final InterviewTemplateService interviewTemplateService;
    private final InterviewService interviewService;
    private final InterviewQuestionService interviewQuestionService;
    private final BlockingQueue<InterviewModel> queue;
    private final CommonNotificationService<InterviewModel> notificationService;
    private final ReportWebClientService reportWebClientService;

    @Transactional
    public InterviewModel start(InterviewTemplateModel interviewTemplateModel, Long userId) {
        log.debug("{} (step 1) --------------------", INTERVIEW_PROCESS_PREFIX);

        var interviewModel = new AtomicReference<InterviewModel>();

        interviewTemplateService.runTemplate(() -> {
            var template = findOrCreateInterviewTemplate(interviewTemplateModel);
            log.debug("Found/created interview template: {}", template);

            log.debug("{} (step 2) --------------------", INTERVIEW_PROCESS_PREFIX);

            var interview = findOrCreateInterview(template, userId);
            log.debug("Interview created with status: {}", interview.getStatus());

            boolean offered = queue.offer(interview);

            if (!offered) {
                log.warn("Failed to add interview to queue. Interview ID: {}", interview.getId());
                throw new MockInterviewException("Failed to queue interview", 500);
            }

            log.debug("{} (step 3) --------------------", INTERVIEW_PROCESS_PREFIX);

            interviewQuestionService.setQuestions(template, interview);
            var startedInterview = interviewService.startInterview(interview.getId(), userId);
            log.debug("Interview questions generated: {}", startedInterview.getQuestions().size());

            interviewModel.set(startedInterview);
        });

        return interviewModel.get();
    }

    private InterviewTemplateModel findOrCreateInterviewTemplate(InterviewTemplateModel templateModel) {
        return interviewTemplateService.find(templateModel).stream()
                .filter(template -> Objects.equals(template.getDifficulty(), templateModel.getDifficulty()))
                .findFirst()
                .orElseGet(() -> {
                    log.info("Creating new interview template for difficulty: {}", templateModel.getDifficulty());
                    return interviewTemplateService.create(templateModel);
                });
    }

    private InterviewModel findOrCreateInterview(InterviewTemplateModel template, Long userId) {
        return Optional.ofNullable(interviewService.findByTemplate(template))
                .orElseGet(() -> {
                    log.info("Creating new interview for template ID: {} and user ID: {}", template.getId(), userId);
                    return interviewService.create(InterviewModel.builder()
                            .templateId(template.getId())
                            .status(InterviewStatus.EMPTY)
                            .userId(userId)
                            .build());
                });
    }

    @Scheduled(cron = "0 */2 * * * ?")
    public void check() {
        if (queue.isEmpty()) {
            return;
        }
        var newQueue = new ConcurrentLinkedQueue<InterviewModel>();
        while (!queue.isEmpty()) {
            InterviewModel interview;
            try {
                interview = queue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new MockInterviewException(500);
            }
            switch (interview.getStatus()) {
                case EMPTY, FILLED -> newQueue.offer(interview);
                case COMPLETED -> AsyncHelper.runAsync(() -> {
                    reportWebClientService.saveReport(CreateReportRequest.builder()
                            .title("Interview evaluation")
                            .interviewId(interview.getId())
                            .format(ReportFormat.PDF)
                            .build());
                    notificationService.send(interview);
                });
                case CANCELLED -> notificationService.send(interview);
                default -> throw new MockInterviewException(500);
            }
            queue.addAll(newQueue);
            if (newQueue.isEmpty()) {
                queue.clear();
            }
            newQueue.clear();
        }
    }

    @PreDestroy
    public void clearQueue() {
        queue.clear();
        log.info("Interview queue cleared");
    }
}