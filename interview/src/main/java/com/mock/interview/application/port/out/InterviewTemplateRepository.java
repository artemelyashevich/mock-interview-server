package com.mock.interview.application.port.out;

import com.mock.interview.lib.model.InterviewTemplateModel;

import java.util.List;

public interface InterviewTemplateRepository {
    InterviewTemplateModel findById(Long id);

    List<InterviewTemplateModel> findAll();

    InterviewTemplateModel save(InterviewTemplateModel template);

    void deleteById(Long id);

    List<InterviewTemplateModel> findAllByDifficulty(String difficulty);

    List<InterviewTemplateModel> findAllByTechnologyStackContaining(String technology);

    List<InterviewTemplateModel> findAllByDifficultyAndTechnologyStackContaining(
        String difficulty,
        String technology
    );
}
