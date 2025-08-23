package com.mock.interview.report.service;

import com.mock.interview.lib.model.ReportFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportFactory {

    private final List<ReportService> reportServices;

    public ReportService getReportService(ReportFormat reportFormat) {
        return reportServices.stream()
                .filter(service -> service.getStatus().equals(reportFormat))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported report format: " + reportFormat
                ));
    }
}
