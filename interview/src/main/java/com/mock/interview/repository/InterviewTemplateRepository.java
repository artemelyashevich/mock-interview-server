package com.mock.interview.repository;

import com.mock.interview.entity.InterviewTemplateEntity;
import com.mock.interview.lib.specification.GenericSpecificationRepository;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterviewTemplateRepository extends GenericSpecificationRepository<InterviewTemplateEntity, Long> {
    List<InterviewTemplateEntity> findAllByDifficulty(String difficulty);

    @Query("""
                    select 1 from InterviewTemplateEntity it where it.title = :title or it.technologyStack = :technologyStack
            """)
    boolean exists(@Param("title") String title, @Param("technologyStack") List<String> technologyStack);

    @Query("""
            select it from InterviewTemplateEntity it where it.title = :title or it.technologyStack = :technologyStack
            """)
    List<InterviewTemplateEntity> find(@NonNull @Param("title") String title, @Param("technologyStack") List<String> technologyStack);
}
