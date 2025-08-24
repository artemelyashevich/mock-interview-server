package com.mock.interview.report.service.generate;

import com.mock.interview.lib.configuration.AppProperties;
import com.mock.interview.lib.dto.CreateReportRequest;
import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.model.ReportModel;
import com.mock.interview.report.mapper.ReportDtoMapper;
import com.mock.interview.report.mapper.ReportEntityMapper;
import com.mock.interview.report.service.PersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ReportFacade {

    private static final ReportDtoMapper reportDtoMapper = Mappers.getMapper(ReportDtoMapper.class);
    private static final ReportEntityMapper reportEntityMapper = Mappers.getMapper(ReportEntityMapper.class);

    @Qualifier("reportGenerationExecutor")
    private final ExecutorService executorService;

    private final ReportFactory reportFactory;
    private final PersistenceService persistenceService;
    private final AppProperties appProperties;

    public ReportFacade(
            ExecutorService executorService,
            ReportFactory reportFactory,
            PersistenceService persistenceService,
            AppProperties appProperties
    ) {
        this.executorService = executorService;
        this.reportFactory = reportFactory;
        this.persistenceService = persistenceService;
        this.appProperties = appProperties;
    }

    public ReportModel generateReport(CreateReportRequest reportRequest) {
        log.debug("Starting report generation for request: {}", reportRequest);

        var service = reportFactory.getReportService(reportRequest.getFormat());

        return CompletableFuture.supplyAsync(() -> generateReportAsync(service, reportRequest), executorService)
                .orTimeout(appProperties.getReportGenerationTimeoutSeconds(), TimeUnit.SECONDS)
                .handle(this::handleGenerationResult)
                .join();
    }

    private ReportModel generateReportAsync(ReportGenerateService service, CreateReportRequest reportRequest) {
        try {
            var reportDto = reportDtoMapper.toModel(reportRequest);
            var persistedReport = persistenceService.create(reportDto);
            var reportModel = reportEntityMapper.toModel(persistedReport);
            var result = service.generate(reportModel);
            log.debug("Successfully generated report for request: {}", reportRequest);
            return result;
        } catch (IOException e) {
            log.error("IO error during report generation for request: {}", reportRequest, e);
            throw new MockInterviewException("IO error during report processing", e, 500);
        } catch (Exception e) {
            log.error("Unexpected error during report generation for request: {}", reportRequest, e);
            throw new MockInterviewException("Unexpected error during report processing", e, 500);
        }
    }

    private ReportModel handleGenerationResult(ReportModel result, Throwable throwable) {
        if (throwable != null) {
            if (throwable.getCause() instanceof MockInterviewException) {
                throw (MockInterviewException) throwable.getCause();
            }
            log.error("Report generation failed with unexpected error", throwable);
            throw new MockInterviewException("Report generation failed", throwable, 500);
        }
        return result;
    }
}