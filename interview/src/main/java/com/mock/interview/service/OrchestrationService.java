package com.mock.interview.service;

import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewStatus;
import com.mock.interview.lib.model.InterviewTemplateModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrchestrationService {

    private final InterviewTemplateService interviewTemplateService;
    private final InterviewService interviewService;
    private final InterviewQuestionService interviewQuestionService;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public InterviewModel start(InterviewTemplateModel interviewTemplateModel, Long userId) {
        log.debug("-------------------- Prepare interview process (step 1) --------------------");

        AtomicReference<InterviewModel> interviewModel = new AtomicReference<>();

        interviewTemplateService.runTemplate(() -> {
            var interviewTemplate = interviewTemplateService.find(interviewTemplateModel).stream()
                    .filter(template -> template.getDifficulty().equals(interviewTemplateModel.getDifficulty()))
                    .toList().getFirst();

            if (interviewTemplate == null) {
                interviewTemplate = interviewTemplateService.create(interviewTemplateModel);
            }

            log.debug("Found (created) interview template: {})", interviewTemplate);

            log.debug("-------------------- Prepare interview process (step 2) --------------------");

            var interview = interviewService.findByTemplate(interviewTemplate);

            if (interview == null) {
                interview = interviewService.create(InterviewModel.builder()
                        .templateId(interviewTemplate.getId())
                        .status(InterviewStatus.EMPTY)
                        .build());
            }

            log.debug("Interview created with {} status", interview.getStatus());

            interviewModel.set(interviewService.startInterview(interview.getId(), userId));

            log.debug("Interview started: {}", interviewModel.get());

            log.debug("-------------------- Prepare interview process (step 3) --------------------");

            interviewQuestionService.setQuestions(interviewTemplate, interviewModel.get());

            log.debug("Interview questions has been generated: {}", interviewModel.get().getQuestions().size());
        });

        return interviewModel.get();
    }
}
