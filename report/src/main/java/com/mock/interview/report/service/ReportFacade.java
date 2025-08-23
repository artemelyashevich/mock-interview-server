package com.mock.interview.report.service;

import com.mock.interview.lib.dto.CreateReportRequest;
import com.mock.interview.lib.model.ReportModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportFacade {

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final ReportFactory reportFactory;

    public ReportModel generateReport(CreateReportRequest reportRequest) throws IOException {
        var service = reportFactory.getReportService(reportRequest.format());
        var reportModelCompletableFuture = CompletableFuture.supplyAsync(() ->
                {
                    try {
                        return service.generate(reportRequest);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                executorService
        );
        try {
            return reportModelCompletableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
