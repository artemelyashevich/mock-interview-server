package com.elyashevich.interview.infrastructure.persistence.adapter;

import com.elyashevich.interview.application.port.out.InterviewQuestionRepository;
import com.elyashevich.interview.infrastructure.persistence.mapper.InterviewQuestionEntityMapper;
import com.elyashevich.interview.infrastructure.persistence.repository.InterviewEntityRepository;
import com.elyashevich.interview.infrastructure.persistence.repository.InterviewQuestionEntityRepository;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.InterviewQuestionModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InterviewQuestionAdapter implements InterviewQuestionRepository {

    private static final InterviewQuestionEntityMapper interviewQuestionEntityMapper = InterviewQuestionEntityMapper.INSTANCE;

    private final InterviewQuestionEntityRepository interviewQuestionEntityRepository;

    @Override
    public List<InterviewQuestionModel> findAll() {
        return interviewQuestionEntityMapper.toModels(interviewQuestionEntityRepository.findAll());
    }

    @Override
    public InterviewQuestionModel findById(Long id) {
        return interviewQuestionEntityMapper.toModel(interviewQuestionEntityRepository.findById(id)
            .orElseThrow(
                () -> {
                    var message = String.format("Could not find interview question with id %d", id);
                    log.debug(message);
                    return new ResourceNotFoundException(message);
                }
            ));
    }

    @Override
    public List<InterviewQuestionModel> findByInterviewId(Long id) {
        return interviewQuestionEntityMapper.toModels(
            interviewQuestionEntityRepository.findAllByInterviewId(id)
        );
    }

    @Override
    public InterviewQuestionModel save(InterviewQuestionModel interviewQuestionModel) {
        return interviewQuestionEntityMapper.toModel(
            interviewQuestionEntityRepository.save(
                interviewQuestionEntityMapper.toEntity(
                    interviewQuestionModel
                )
            )
        );
    }

    @Override
    public void delete(InterviewQuestionModel interviewQuestionModel) {
        interviewQuestionEntityRepository.deleteById(interviewQuestionModel.getId());
    }
}
