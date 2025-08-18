package com.elyashevich.interview.infrastructure.web.controller;

import com.elyashevich.interview.application.port.in.InterviewService;
import com.elyashevich.interview.infrastructure.web.mapper.InterviewDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private static final InterviewDtoMapper mapper = InterviewDtoMapper.INSTANCE;

    private final InterviewService interviewService;
}
