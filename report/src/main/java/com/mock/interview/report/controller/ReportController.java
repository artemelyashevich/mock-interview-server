package com.mock.interview.report.controller;

import com.mock.interview.lib.dto.CreateReportRequest;
import com.mock.interview.lib.model.ReportModel;
import com.mock.interview.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/interview/{id}")
    public List<ReportModel> findAllByInterviewId(@PathVariable Long id) {
        return reportService.findAllByInterviewId(id);
    }

    @GetMapping("/{id}")
    public ReportModel findById(@PathVariable Long id) {
        return reportService.findById(id);
    }

    @PostMapping
    public ReportModel create(@Validated @RequestBody CreateReportRequest createReportRequest) {
        return reportService.create(createReportRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        reportService.deleteById(id);
    }
}
