package com.mock.interview.infrastructure.web.controller;

import com.elyashevich.interview.application.port.in.InterviewTemplateService;
import com.elyashevich.interview.infrastructure.web.mapper.InterviewTemplateDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/interviewTemplates")
@RequiredArgsConstructor
public class InterviewTemplateController {

    private static final InterviewTemplateDtoMapper mapper = InterviewTemplateDtoMapper.INSTANCE;

    private final InterviewTemplateService interviewTemplateService;
}
