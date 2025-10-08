package com.mock.interview.report.service.impl;

import com.mock.interview.lib.dto.CreateReportRequest;
import com.mock.interview.lib.model.InterviewReportStatusModel;
import com.mock.interview.lib.model.ReportModel;
import com.mock.interview.lib.model.ReportStatus;
import com.mock.interview.lib.util.JsonHelper;
import com.mock.interview.report.mapper.ReportDtoMapper;
import com.mock.interview.report.mapper.ReportEntityMapper;
import com.mock.interview.report.service.PersistenceService;
import com.mock.interview.report.service.ReportService;
import com.mock.interview.report.service.generate.ReportFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final ReportEntityMapper reportDtoMapper = ReportEntityMapper.INSTANCE;

    private final ReportFacade reportFacade;
    private final PersistenceService persistenceService;

    @Override
    public ReportModel create(CreateReportRequest createReportRequest) {
        log.debug("Attempting to create report: {}", createReportRequest);

        var model = reportFacade.generateReport(createReportRequest);

        log.debug("Created report: {}", model);
        return model;
    }

    @Override
    public ReportModel findById(Long id) {
        log.debug("Attempting to find report: {}", id);

        var reportModel = persistenceService.findById(id);

        log.debug("Report found: {}", reportModel);
        return reportDtoMapper.toModel(reportModel);
    }

    @Override
    public List<ReportModel> findAllByInterviewId(Long id) {
        log.debug("Attempting to find all reports by interview id: {}", id);

        var models = persistenceService.findByInterviewId(id);

        log.debug("Found all reports by interview id: {}", models.size());
        return reportDtoMapper.toModels(models);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Attempting to delete report: {}", id);
        persistenceService.deleteById(id);
        log.debug("Deleted report: {}", id);
    }
}
