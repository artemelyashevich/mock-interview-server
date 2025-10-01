package com.mock.interview.entity;

import com.mock.interview.lib.contract.AbstractEntity;
import com.mock.interview.lib.model.InterviewStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long templateId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "overall_score")
    private Double overallScore;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    @Builder.Default
    private List<InterviewQuestionEntity> questions = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private Long reportId;
}
