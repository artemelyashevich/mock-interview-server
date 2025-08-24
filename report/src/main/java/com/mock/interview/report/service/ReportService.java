package com.mock.interview.report.service;

import com.mock.interview.lib.dto.CreateReportRequest;
import com.mock.interview.lib.model.ReportModel;

import java.io.IOException;
import java.util.List;

public interface ReportService {

    ReportModel create(CreateReportRequest createReportRequest);

    ReportModel findById(Long id);

    List<ReportModel> findAllByInterviewId(Long id);

    void deleteById(Long id);
}
