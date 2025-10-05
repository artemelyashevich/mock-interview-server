package com.mock.interview.service;

import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.model.InterviewTemplateModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ai-service")
public interface AiWebClientService {

    @PostMapping("/api/v1/ai/questions")
    List<InterviewQuestionModel> generateQuestions(final @RequestBody InterviewTemplateModel template);
}
