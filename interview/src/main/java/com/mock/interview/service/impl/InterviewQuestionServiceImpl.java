package com.mock.interview.service.impl;

import com.mock.interview.configuration.ApplicationProps;
import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.model.EvaluationModel;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.model.InterviewTemplateModel;
import com.mock.interview.lib.util.JsonHelper;
import com.mock.interview.lib.util.RestHelper;
import com.mock.interview.mapper.InterviewQuestionEntityMapper;
import com.mock.interview.repository.InterviewQuestionRepository;
import com.mock.interview.service.InterviewQuestionService;
import com.mock.interview.service.InterviewService;
import com.mock.interview.service.AiWebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewQuestionServiceImpl implements InterviewQuestionService {

    private static final InterviewQuestionEntityMapper mapper = InterviewQuestionEntityMapper.INSTANCE;

    private final InterviewQuestionRepository interviewQuestionRepository;

    @Lazy
    private final InterviewService interviewService;

    private final RestTemplate restTemplate;

    private final ApplicationProps applicationProps;

    @Lazy
    private final AiWebClientService aiClientService;

    @Override
    public List<InterviewQuestionModel> batchCreate(List<InterviewQuestionModel> interviewQuestionModels) throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() ->
                interviewQuestionModels.stream().map(interview -> {
                            try {
                                return mapper.toModel(interviewQuestionRepository.save(mapper.toEntity(interview)));
                            } catch (Exception e) {
                                log.error("Failed to save interview {}: {}", interview.getId(), e.getMessage());
                                return null;
                            }
                        }).filter(Objects::nonNull).toList()
        ).get();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Caching(
            put = {
                    @CachePut(value = "InterviewQuestionService::evaluateQuestion", key = "#questionId"),
                    @CachePut(value = "InterviewQuestionService::findCurrentQuestion", key = "#interviewQuestion.getId()")
            }
    )
    public InterviewQuestionModel create(Long questionId, Long userId, InterviewQuestionModel interviewQuestionModel) {
        log.debug("Attempting to create interview question");

        log.debug("Created interview question");
        return null;
    }

    @Override
    @Transactional
    @Caching(
            put = {
                    @CachePut(value = "InterviewQuestionService::evaluateQuestion", key = "#questionId"),
                    @CachePut(value = "InterviewQuestionService::findCurrentQuestion", key = "#interviewQuestion.getId()")
            }
    )
    public InterviewQuestionModel saveAnswer(Long questionId, Long userId, String answer) {
        log.debug("Attempting to save interview question");

        log.debug("Updated interview question");
        return null;
    }

    @Override
    @Transactional
    @Cacheable(value = "InterviewQuestionService::evaluateQuestion", key = "#questionId")
    public InterviewQuestionModel evaluateQuestion(Long questionId, EvaluationModel evaluation, Long evaluatorId) {
        log.debug("Updated interview question evaluation");
        return null;
    }

    @Override
    @Cacheable(value = "InterviewQuestionService::findCurrentQuestion", key = "#interviewId")
    public InterviewQuestionModel findCurrentQuestion(Long interviewId) {
        log.debug("Attempting to find current interview question");

        log.debug("Found current interview question");
        return null;
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void delete(Long interviewId) {
        log.debug("Attempting to delete interview question: {}", interviewId);


        log.debug("Deleted interview question");
    }

    @Override
    @SuppressWarnings("all")
    public void setQuestions(InterviewTemplateModel interviewTemplate, InterviewModel startedInterview) {
        List<InterviewQuestionModel> response = new ArrayList<>();
        if (applicationProps.isExternalAi()) {
            var res = restTemplate.exchange(
                    applicationProps.getExternalAiUrl(),
                    HttpMethod.POST,
                    RestHelper.createHttpEntity(interviewTemplate),
                    String.class
            );
            if (res.getStatusCode() == HttpStatus.OK) {
                var list = (List<String>) JsonHelper.fromJson(res.getBody(), List.class);
                if (list != null && !list.isEmpty()) {
                    list.forEach(q -> response.add(InterviewQuestionModel.builder().questionText(q).build()));
                }
            } else {
                throw new MockInterviewException("Error in external ai service: %s".formatted(res.getBody()), 500);
            }
        } else {
            response.addAll(aiClientService.generateQuestions(interviewTemplate));
            if (response.isEmpty()) {
                throw new MockInterviewException("Error in internal ai service", 500);
            }
        }
        try {
            var questions = batchCreate(response);
            startedInterview.setQuestions(questions);
        } catch (ExecutionException | InterruptedException e) {
            throw new MockInterviewException("Unable save all questions", e, 500);
        }
    }

    private void updateInterviewOverallScore(Long interviewId) {
    }
}
