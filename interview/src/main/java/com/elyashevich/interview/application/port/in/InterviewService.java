package com.elyashevich.interview.application.port.in;

import com.elyashevich.interview.infrastructure.persistence.entity.InterviewEntity;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.specification.SearchCriteria;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface InterviewService {

    InterviewModel create(InterviewModel interviewModel);

    CompletableFuture<List<InterviewModel>> batchSaveAsync(List<InterviewModel> interviews);

    InterviewModel addQuestion(Long interviewId, Long userId, InterviewQuestionModel interviewQuestionModel);

    InterviewModel startInterview(Long interviewId, Long userId);

    InterviewModel completeInterview(Long interviewId, Long userId);

    InterviewModel findById(Long interviewId);

    void save(InterviewModel interview);

    Page<InterviewEntity> search(SearchCriteria searchCriteria);

    List<InterviewEntity> searchAll(SearchCriteria searchCriteria);
}
