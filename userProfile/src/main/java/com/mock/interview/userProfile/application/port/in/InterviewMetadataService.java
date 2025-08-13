package com.mock.interview.userProfile.application.port.in;

import com.mock.interview.lib.model.InterviewMetadataModel;

import java.util.List;

public interface InterviewMetadataService {
    List<InterviewMetadataModel> findAll();

    List<InterviewMetadataModel> findByUserId(Long userId);

    InterviewMetadataModel findById(Long id);

    InterviewMetadataModel save(InterviewMetadataModel interviewMetadataModel);

    void delete(Long id);
}
