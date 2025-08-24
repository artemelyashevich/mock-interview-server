package com.mock.interview.report.service.generate;

import com.mock.interview.lib.model.ReportFormat;
import com.mock.interview.lib.model.ReportModel;

import java.io.IOException;

public interface ReportGenerateService {

    ReportModel generate(ReportModel createReportRequest) throws IOException;

    ReportFormat getStatus();
}
