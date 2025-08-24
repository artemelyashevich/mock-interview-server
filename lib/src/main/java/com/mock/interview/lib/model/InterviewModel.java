package com.mock.interview.lib.model;

import com.mock.interview.lib.contract.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@With
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
public class InterviewModel extends AbstractModel {

    @EqualsAndHashCode.Include
    private Long id;

    private Long userId;

    private Long templateId;

    @Setter
    private InterviewStatus status;

    @Setter
    private LocalDateTime startTime;

    @Setter
    private LocalDateTime endTime;

    @Setter
    private Double overallScore;

    @Builder.Default
    private List<InterviewQuestionModel> questions = Collections.emptyList();

    private Long reportId;

    public boolean isInProgress() {
        return status == InterviewStatus.IN_PROGRESS;
    }

    public boolean isCompleted() {
        return status == InterviewStatus.COMPLETED;
    }

    public void completeInterview() {
        if (this.status != InterviewStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only interviews in progress can be completed");
        }
        this.status = InterviewStatus.COMPLETED;
        this.endTime = LocalDateTime.now();
    }

    public void addQuestion(InterviewQuestionModel question) {
        this.questions.add(question);
    }
}
