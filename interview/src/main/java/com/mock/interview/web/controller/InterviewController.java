package com.mock.interview.web.controller;

import com.mock.interview.lib.annotation.Authenticated;
import com.mock.interview.lib.dto.CreateInterviewRequest;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.service.interview.InterviewService;
import com.mock.interview.web.mapper.InterviewWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private static final InterviewWebMapper mapper = InterviewWebMapper.INSTANCE;

    private final InterviewService interviewService;

    @PostMapping
    @Authenticated
    public InterviewModel create(@Validated @RequestBody CreateInterviewRequest createInterviewRequest) {
        return interviewService.create(mapper.toModel(createInterviewRequest));
    }

    @PostMapping("/start/{userId}/{interviewId}")
    @Authenticated
    public InterviewModel start(@PathVariable Long userId, @PathVariable Long interviewId) {
        return interviewService.startInterview(userId, interviewId);
    }
}
