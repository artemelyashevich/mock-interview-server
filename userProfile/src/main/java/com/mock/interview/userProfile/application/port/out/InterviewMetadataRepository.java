package com.mock.interview.userProfile.application.port.out;

import com.mock.interview.lib.model.InterviewMetadataModel;

import java.util.List;

public interface InterviewMetadataRepository {

    List<InterviewMetadataModel> findAll();

    List<InterviewMetadataModel> findByUserId(Long userId);

    InterviewMetadataModel findById(Long id);

    InterviewMetadataModel save(InterviewMetadataModel interviewMetadataModel);

    void delete(InterviewMetadataModel interviewMetadataModel);
}
