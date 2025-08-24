package com.mock.interview.infrastructure.persistence.repository;

import com.mock.interview.infrastructure.persistence.entity.InterviewQuestionEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterviewQuestionEntityRepository extends JpaRepository<InterviewQuestionEntity, Long> {

    @Query("SELECT q FROM InterviewQuestionEntity q WHERE q.interview.id = :interviewId")
    List<InterviewQuestionEntity> findAllByInterviewId(@Param("interviewId") Long interviewId);

    @Query("""
        SELECT q FROM InterviewQuestionEntity q 
        WHERE q.interview.id = :interviewId 
        AND LOWER(q.topic) LIKE LOWER(CONCAT('%', :topic, '%'))
        """)
    List<InterviewQuestionEntity> findAllByInterviewIdAndTopicContaining(
        @Param("interviewId") Long interviewId,
        @Param("topic") String topic
    );

    void deleteAllByInterviewId(Long interviewId);

    @Query("""
        SELECT q FROM InterviewQuestionEntity q 
        WHERE q.interview.id = :interviewId 
        ORDER BY q.orderIndex DESC LIMIT 1
        """)
    Optional<InterviewQuestionEntity> findLatestByInterviewId(@Param("interviewId") Long interviewId);

    @Query("""
        SELECT q FROM InterviewQuestionEntity q 
        WHERE q.interview.id = :interviewId 
        ORDER BY q.orderIndex ASC LIMIT 1
        """)
    Optional<InterviewQuestionEntity> findNextQuestion(@Param("interviewId") Long interviewId);

    Long countByInterviewId(Long interviewId);

    @Query("""
        SELECT COUNT(q) FROM InterviewQuestionEntity q 
        WHERE q.interview.id = :interviewId 
        AND q.answerText IS NOT NULL
        """)
    Long countAnsweredQuestionsByInterviewId(@Param("interviewId") Long interviewId);

    @Query("""
        SELECT AVG(CAST(JSON_EXTRACT(q.evaluation, '$.score') AS double)) 
        FROM InterviewQuestionEntity q 
        WHERE q.interview.id = :interviewId 
        AND q.evaluation IS NOT NULL
        """)
    Double calculateAverageScoreByInterviewId(@Param("interviewId") Long interviewId);

    @Query("""
        SELECT q FROM InterviewQuestionEntity q 
        WHERE q.interview.id = :interviewId 
        ORDER BY q.orderIndex ASC
        """)
    Page<InterviewQuestionEntity> findByInterviewIdWithPagination(
        @Param("interviewId") Long interviewId,
        Pageable pageable
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT q FROM InterviewQuestionEntity q 
        WHERE q.id = :id 
        AND q.interview.id = :interviewId
        """)
    Optional<InterviewQuestionEntity> findByIdAndInterviewIdWithLock(
        @Param("id") Long id,
        @Param("interviewId") Long interviewId
    );
}
