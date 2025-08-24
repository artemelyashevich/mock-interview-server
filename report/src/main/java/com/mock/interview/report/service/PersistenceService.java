package com.mock.interview.report.service;

import com.mock.interview.lib.model.ReportModel;
import com.mock.interview.report.entity.ReportEntity;

import java.util.List;

public interface PersistenceService {

    ReportEntity create(ReportModel reportModel);

    ReportEntity findById(Long id);

    List<ReportEntity> findByInterviewId(Long interviewId);

    void deleteById(Long id);
}
