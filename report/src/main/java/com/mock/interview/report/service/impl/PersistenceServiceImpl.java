package com.mock.interview.report.service.impl;

import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.ReportModel;
import com.mock.interview.report.entity.ReportEntity;
import com.mock.interview.report.mapper.ReportEntityMapper;
import com.mock.interview.report.repository.ReportEntityRepository;
import com.mock.interview.report.service.PersistenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersistenceServiceImpl implements PersistenceService {

    private static final ReportEntityMapper reportEntityMapper = Mappers.getMapper(ReportEntityMapper.class);

    private final ReportEntityRepository reportEntityRepository;

    @Override
    public ReportEntity create(ReportModel reportModel) {
        return reportEntityRepository.save(reportEntityMapper.toEntity(reportModel));
    }

    @Override
    public ReportEntity findById(Long id) {
        return reportEntityRepository.findById(id).orElseThrow(
                () -> {
                    var message = "Report with id: '%s' not found".formatted(id);
                    log.debug(message);
                    return new ResourceNotFoundException(message);
                }
        );
    }

    @Override
    public List<ReportEntity> findByInterviewId(Long interviewId) {
        return reportEntityRepository.findByInterviewId(interviewId);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (reportEntityRepository.findById(id).isEmpty()) {
            var message = "Report with id: '%s' not found".formatted(id);
            log.debug(message);
            throw new ResourceNotFoundException(message);
        }
        reportEntityRepository.deleteById(id);
    }
}
