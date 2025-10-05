package com.mock.interview.service.web;

import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.model.InterviewReportStatusModel;
import com.mock.interview.lib.model.ReportModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "report-service")
public interface ReportWebClientService {

    @PostMapping("/api/v1/reports/save")
    List<InterviewQuestionModel> saveReport(final @RequestBody ReportModel reportModel);

    @GetMapping("/api/v1/reports/{id}/status")
    InterviewReportStatusModel findCurrentStatus(final @PathVariable("id") Long id);
}
