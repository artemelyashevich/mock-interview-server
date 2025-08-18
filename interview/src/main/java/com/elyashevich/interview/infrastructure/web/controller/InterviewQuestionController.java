package com.elyashevich.interview.infrastructure.web.controller;

import com.elyashevich.interview.application.port.in.InterviewQuestionService;
import com.elyashevich.interview.infrastructure.web.mapper.InterviewQuestionDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/interviewQuestion")
@RequiredArgsConstructor
public class InterviewQuestionController {

    private static final InterviewQuestionDtoMapper mapper = InterviewQuestionDtoMapper.INSTANCE;

    private final InterviewQuestionService interviewQuestionService;
}
