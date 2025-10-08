package com.mock.interview.service.web;

import com.mock.interview.lib.dto.CreateReportRequest;
import com.mock.interview.lib.model.InterviewQuestionModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "report-service")
public interface ReportWebClientService {

    @PostMapping("/api/v1/reports")
    List<InterviewQuestionModel> saveReport(final @RequestBody CreateReportRequest reportModel);
}
