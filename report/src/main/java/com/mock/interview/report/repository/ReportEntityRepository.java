package com.mock.interview.report.repository;

import com.mock.interview.report.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportEntityRepository extends JpaRepository<ReportEntity, Long> {

    @Query("select r from ReportEntity r where r.interviewId = :interviewId")
    List<ReportEntity> findByInterviewId(Long interviewId);
}
