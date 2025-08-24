package com.mock.interview.infrastructure.persistence.repository;

import com.mock.interview.infrastructure.persistence.entity.InterviewEntity;
import com.mock.interview.lib.model.InterviewProjection;
import com.mock.interview.lib.model.InterviewStatus;
import com.mock.interview.lib.specification.GenericSpecificationRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewEntityRepository extends GenericSpecificationRepository<InterviewEntity, Long> {

    List<InterviewEntity> findAllByStatus(InterviewStatus status);

    @Query("SELECT i FROM InterviewEntity i WHERE i.userId = :userId AND i.status = :status ORDER BY i.startTime DESC")
    List<InterviewEntity> findAllByUserIdAndStatus(
        @Param("userId") Long userId,
        @Param("status") InterviewStatus status
    );

    @EntityGraph(attributePaths = {"template", "questions"})
    Optional<InterviewEntity> findByIdAndUserId(Long id, Long userId);

    @Query("""
        SELECT i FROM InterviewEntity i
        WHERE i.startTime BETWEEN :start AND :end 
        ORDER BY i.startTime ASC
        """)
    List<InterviewEntity> findAllInTimeRange(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT i FROM InterviewEntity i 
        WHERE i.endTime < :endTime AND i.status = :status 
        ORDER BY i.endTime DESC
        """)
    List<InterviewEntity> findCompletedBeforeDate(
        @Param("endTime") LocalDateTime endTime,
        @Param("status") InterviewStatus status
    );

    Long countByUserIdAndStatus(Long userId, InterviewStatus status);

    @Query("""
        SELECT COUNT(i) FROM InterviewEntity i 
        WHERE i.userId = :userId 
        AND i.status = :status 
        AND i.startTime > :afterDate
        """)
    Long countRecentByUserAndStatus(
        @Param("userId") Long userId,
        @Param("status") InterviewStatus status,
        @Param("afterDate") LocalDateTime afterDate
    );

    Page<InterviewEntity> findAllByStatus(InterviewStatus status, Pageable pageable);

    @Query("""
        SELECT i FROM InterviewEntity i 
        WHERE i.userId = :userId 
        AND (:status IS NULL OR i.status = :status)
        """)
    Page<InterviewEntity> findByUserIdWithOptionalStatus(
        @Param("userId") Long userId,
        @Param("status") InterviewStatus status,
        Pageable pageable
    );

    @EntityGraph(attributePaths = {"template"})
    @Query("SELECT i FROM InterviewEntity i WHERE i.id = :id")
    Optional<InterviewEntity> findByIdWithTemplate(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM InterviewEntity i WHERE i.id = :id")
    Optional<InterviewEntity> findByIdForUpdate(@Param("id") Long id);

    @Query("""
        SELECT i.id as id, i.status as status, COUNT(q) as questionCount 
        FROM InterviewEntity i LEFT JOIN i.questions q 
        WHERE i.userId = :userId 
        GROUP BY i.id, i.status
        """)
    List<InterviewProjection> findInterviewSummariesByUserId(@Param("userId") Long userId);
}
