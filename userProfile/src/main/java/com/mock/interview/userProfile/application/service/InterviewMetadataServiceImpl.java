package com.mock.interview.userProfile.application.service;

import com.mock.interview.lib.model.InterviewMetadataModel;
import com.mock.interview.userProfile.application.port.in.InterviewMetadataService;
import com.mock.interview.userProfile.application.port.out.InterviewMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewMetadataServiceImpl implements InterviewMetadataService {

    private final InterviewMetadataRepository interviewMetadataRepository;

    @Override
    public List<InterviewMetadataModel> findAll() {
        log.debug("Attempting to find all InterviewMetadataModels");

        var items = interviewMetadataRepository.findAll();

        log.debug("Found {} InterviewMetadataModels", items.size());
        return items;
    }

    @Override
    public List<InterviewMetadataModel> findByUserId(Long userId) {
        log.debug("Attempting to find InterviewMetadataModels by userId {}", userId);

        var item = interviewMetadataRepository.findByUserId(userId);

        log.debug("Found {} InterviewMetadataModels by userId", item.size());
        return item;
    }

    @Override
    public InterviewMetadataModel findById(Long id) {
        log.debug("Attempting to find InterviewMetadataModels by id {}", id);

        var item = interviewMetadataRepository.findById(id);

        log.debug("Found {} InterviewMetadataModel by id", item);
        return item;
    }

    @Override
    public InterviewMetadataModel save(InterviewMetadataModel interviewMetadataModel) {
        log.debug("Attempting to save InterviewMetadataModel {}", interviewMetadataModel);

        var item = interviewMetadataRepository.save(interviewMetadataModel);

        log.debug("Saved InterviewMetadataModel {}", item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Attempting to delete InterviewMetadataModel {}", id);

        var item = findById(id);

        interviewMetadataRepository.delete(item);

        log.debug("Deleted InterviewMetadataModel {}", id);
    }
}
