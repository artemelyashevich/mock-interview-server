package com.mock.interview.userProfile.infrastructure.persistence.adapter;

import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.InterviewMetadataModel;
import com.mock.interview.userProfile.application.port.out.InterviewMetadataRepository;
import com.mock.interview.userProfile.infrastructure.persistence.mapper.InterviewMetadataEntityMapper;
import com.mock.interview.userProfile.infrastructure.persistence.repository.InterviewMetadataEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InterviewMetadataRepositoryAdapter implements InterviewMetadataRepository {

    private static final InterviewMetadataEntityMapper interviewMetadataEntityMapper = InterviewMetadataEntityMapper.INSTANCE;

    private final InterviewMetadataEntityRepository interviewMetadataEntityRepository;

    @Override
    public List<InterviewMetadataModel> findAll() {
        return interviewMetadataEntityMapper.toModels(interviewMetadataEntityRepository.findAll());
    }

    @Override
    public List<InterviewMetadataModel> findByUserId(Long userId) {
        return interviewMetadataEntityMapper.toModels(
                interviewMetadataEntityRepository.findAllByUserId(userId)
        );
    }

    @Override
    public InterviewMetadataModel findById(Long id) {
        return interviewMetadataEntityMapper.toModel(
                interviewMetadataEntityRepository.findById(id).orElseThrow(
                        () -> {
                            var message = String.format("Interview Metadata Entity with id %s not found", id);
                            log.debug(message);
                            return new ResourceNotFoundException(message);
                        }
                )
        );
    }

    @Override
    public InterviewMetadataModel save(InterviewMetadataModel interviewMetadataModel) {
        return interviewMetadataEntityMapper.toModel(
                interviewMetadataEntityRepository.save(
                        interviewMetadataEntityMapper.toEntity(interviewMetadataModel)
                )
        );
    }

    @Override
    public void delete(InterviewMetadataModel interviewMetadataModel) {
        interviewMetadataEntityRepository.deleteById(interviewMetadataModel.getId());
    }
}
