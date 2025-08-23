package com.mock.interview.report.service;

import com.mock.interview.lib.dto.CreateReportRequest;
import com.mock.interview.lib.model.ReportFormat;
import com.mock.interview.lib.model.ReportModel;

import java.io.IOException;

public interface ReportService {

    ReportModel generate(CreateReportRequest createReportRequest) throws IOException;

    ReportFormat getStatus();
}
