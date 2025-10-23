package com.mock.interview.service.interview.impl;

import com.mock.interview.configuration.ApplicationProps;
import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.model.EvaluationModel;
import com.mock.interview.lib.model.InterviewAnswersModel;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.model.InterviewTemplateModel;
import com.mock.interview.lib.util.JsonHelper;
import com.mock.interview.lib.util.RestHelper;
import com.mock.interview.mapper.InterviewQuestionEntityMapper;
import com.mock.interview.repository.InterviewQuestionRepository;
import com.mock.interview.service.interview.InterviewQuestionService;
import com.mock.interview.service.web.AiWebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewQuestionServiceImpl implements InterviewQuestionService {

    private static final InterviewQuestionEntityMapper mapper = InterviewQuestionEntityMapper.INSTANCE;

    private final InterviewQuestionRepository interviewQuestionRepository;

    private final RestTemplate restTemplate;

    private final ApplicationProps applicationProps;

    @Lazy
    private final AiWebClientService aiClientService;

    @Override
    public List<InterviewQuestionModel> batchCreate(List<InterviewQuestionModel> interviewQuestionModels) throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() ->
                interviewQuestionModels.stream().map(question -> {
                    try {
                        return mapper.toModel(interviewQuestionRepository.save(mapper.toEntity(question)));
                    } catch (Exception e) {
                        log.error("Failed to save interview {}: {}", question.getId(), e.getMessage());
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
    public InterviewQuestionModel saveAnswer(Long questionId, String answer) {
        log.debug("Attempting to save interview question");
        var question = interviewQuestionRepository.findById(questionId).orElseThrow(() -> new MockInterviewException(404));
        question.setAnswerText(answer);
        log.debug("Updated interview question");
        return mapper.toModel(interviewQuestionRepository.save(question));
    }

    @Override
    @Async
    @Transactional
    @Cacheable(value = "InterviewQuestionService::evaluateQuestions", key = "#interviewId")
    public List<EvaluationModel> evaluateQuestions(Long interviewId) {
        log.debug("Starting evaluation for interview ID: {}", interviewId);

        var questions = interviewQuestionRepository.findAllByInterviewId(interviewId);

        log.debug("Found {} questions for interview ID: {}", questions.size(), interviewId);

        var questionModels = mapper.toModels(questions);
        var interviewAnswers = InterviewAnswersModel.builder()
                .questions(questionModels)
                .build();

        var future = CompletableFuture.supplyAsync(() -> {
            try {
                log.debug("Submitting evaluation task to AI service");
                return aiClientService.generateAnswers(interviewAnswers);
            } catch (Exception e) {
                log.error("AI service evaluation failed", e);
                throw new MockInterviewException("AI evaluation service unavailable", e, 503);
            }
        });

        try {
            var result = future.get(30, TimeUnit.SECONDS);
            log.debug("Successfully evaluated {} answers for interview ID: {}", result.size(), interviewId);
            return result;
        } catch (TimeoutException e) {
            log.error("Evaluation timeout for interview ID: {}", interviewId, e);
            throw new MockInterviewException("Evaluation timeout - service unavailable", e, 503);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Evaluation was interrupted for interview ID: {}", interviewId, e);
            throw new MockInterviewException("Evaluation process interrupted", e, 500);
        } catch (ExecutionException e) {
            log.error("Evaluation execution failed for interview ID: {}", interviewId, e);
            throw new MockInterviewException("Evaluation execution failed", e, 500);
        } catch (Exception e) {
            log.error("Unexpected error during evaluation for interview ID: {}", interviewId, e);
            throw new MockInterviewException("Unexpected evaluation error", e, 500);
        }
    }

    @Override
    @Cacheable(value = "InterviewQuestionService::findCurrentQuestion", key = "#interviewId")
    public InterviewQuestionModel findCurrentQuestion(Long interviewId) {
        log.debug("Attempting to find current interview question");
        var current = interviewQuestionRepository.findCurrent(interviewId).orElseThrow(() -> new MockInterviewException(400));
        log.debug("Found current interview question");
        return mapper.toModel(current);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void delete(Long interviewId) {
        log.debug("Attempting to delete interview question: {}", interviewId);

        var interviewQuestion = interviewQuestionRepository.findById(interviewId).orElseThrow(
                () -> new MockInterviewException(404)
        );

        interviewQuestionRepository.delete(interviewQuestion);

        log.debug("Deleted interview question");
    }

    @Override
    public void setQuestions(InterviewTemplateModel interviewTemplate, InterviewModel startedInterview) {
        validateInputParameters(interviewTemplate, startedInterview);

        var questions = generateQuestions(interviewTemplate);
        validateGeneratedQuestions(questions);

        var savedQuestions = saveQuestionsBatch(startedInterview.getId(), questions);
        startedInterview.setQuestions(savedQuestions);

        log.debug("Successfully set {} questions for interview ID: {}",
                savedQuestions.size(), startedInterview.getId());
    }

    private void validateInputParameters(InterviewTemplateModel interviewTemplate, InterviewModel startedInterview) {
        if (interviewTemplate == null) {
            throw new IllegalArgumentException("Interview template cannot be null");
        }
        if (startedInterview == null) {
            throw new IllegalArgumentException("Interview model cannot be null");
        }
    }

    private List<InterviewQuestionModel> generateQuestions(InterviewTemplateModel interviewTemplate) {
        try {
            if (applicationProps.isExternalAi()) {
                return generateQuestionsExternal(interviewTemplate);
            } else {
                return generateQuestionsInternal(interviewTemplate);
            }
        } catch (Exception e) {
            throw new MockInterviewException("Failed to generate questions for interview template", e, 500);
        }
    }

    private List<InterviewQuestionModel> generateQuestionsExternal(InterviewTemplateModel interviewTemplate) {
        var requestEntity = RestHelper.createHttpEntity(interviewTemplate);

        var response = restTemplate.exchange(
                applicationProps.getExternalAiUrl(),
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            var errorMessage = "External AI service returned status: %s. Response: %s"
                    .formatted(response.getStatusCode(), response.getBody());
            throw new MockInterviewException(errorMessage, 500);
        }

        var responseBody = response.getBody();
        if (responseBody == null || responseBody.isBlank()) {
            throw new MockInterviewException("External AI service returned empty response", 500);
        }

        var questionList = parseQuestionResponse(responseBody);
        return mapToQuestionModels(questionList);
    }

    @SuppressWarnings("unchecked")
    private List<String> parseQuestionResponse(String responseBody) {
        try {
            var questionList = (List<String>) JsonHelper.fromJson(responseBody, List.class);

            if (questionList == null) {
                throw new MockInterviewException("Failed to parse external AI response: null result", 500);
            }

            return questionList;
        } catch (Exception e) {
            throw new MockInterviewException("Failed to parse external AI response", e, 500);
        }
    }

    private List<InterviewQuestionModel> generateQuestionsInternal(InterviewTemplateModel interviewTemplate) {
        var questions = aiClientService.generateQuestions(interviewTemplate);

        if (questions == null) {
            throw new MockInterviewException("Internal AI service returned null response", 500);
        }

        return questions;
    }

    private List<InterviewQuestionModel> mapToQuestionModels(List<String> questionTexts) {
        return questionTexts.stream()
                .filter(questionText -> questionText != null && !questionText.isBlank())
                .map(questionText -> InterviewQuestionModel.builder()
                        .questionText(questionText.trim())
                        .build())
                .toList();
    }

    private void validateGeneratedQuestions(List<InterviewQuestionModel> questions) {
        if (questions == null || questions.isEmpty()) {
            throw new MockInterviewException("No questions were generated", 500);
        }

        var validQuestions = questions.stream()
                .filter(question -> question != null && !question.getQuestionText().isBlank())
                .toList();

        if (validQuestions.isEmpty()) {
            throw new MockInterviewException("All generated questions are invalid or empty", 500);
        }

        log.debug("Generated {} valid questions out of {}", validQuestions.size(), questions.size());
    }

    private List<InterviewQuestionModel> saveQuestionsBatch(Long interviewId, List<InterviewQuestionModel> questions) {
        try {
            var q = questions.stream()
                    .map(question -> question.withInterviewId(interviewId))
                    .toList();
            return batchCreate(q);
        } catch (ExecutionException e) {
            throw new MockInterviewException("Failed to save questions due to execution error", e, 500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MockInterviewException("Question saving was interrupted", e, 500);
        } catch (Exception e) {
            throw new MockInterviewException("Unexpected error while saving questions", e, 500);
        }
    }
}
