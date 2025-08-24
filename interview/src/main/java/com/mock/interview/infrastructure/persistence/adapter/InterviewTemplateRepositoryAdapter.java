package com.mock.interview.infrastructure.persistence.adapter;

import com.mock.interview.application.port.out.InterviewTemplateRepository;
import com.mock.interview.infrastructure.persistence.mapper.InterviewTemplateEntityMapper;
import com.mock.interview.infrastructure.persistence.repository.InterviewTemplateEntityRepository;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.InterviewTemplateModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InterviewTemplateRepositoryAdapter implements InterviewTemplateRepository {
    private static final InterviewTemplateEntityMapper MAPPER = InterviewTemplateEntityMapper.INSTANCE;
    private static final String TEMPLATE_NOT_FOUND_MSG = "Could not find interview template with id %d";

    private final InterviewTemplateEntityRepository repository;

    @Override
    public InterviewTemplateModel findById(Long id) throws ResourceNotFoundException {
        return repository.findById(id)
                .map(MAPPER::toModel)
                .orElseThrow(() -> {
                    String message = String.format(TEMPLATE_NOT_FOUND_MSG, id);
                    log.debug(message);
                    return new ResourceNotFoundException(message);
                });
    }

    @Override
    public List<InterviewTemplateModel> findAll() {
        return MAPPER.toModels(repository.findAll());
    }

    @Override
    public InterviewTemplateModel save(InterviewTemplateModel template) {
        return MAPPER.toModel(repository.save(MAPPER.toEntity(template)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<InterviewTemplateModel> findAllByDifficulty(String difficulty) {
        return MAPPER.toModels(repository.findAllByDifficulty(difficulty));
    }
}
