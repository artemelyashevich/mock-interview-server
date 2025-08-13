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
    private InterviewId id;

    private UserId userId;

    private InterviewTemplateId templateId;

    @Setter
    private InterviewStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Setter
    private Double overallScore;

    @Builder.Default
    private List<InterviewQuestionModel> questions = Collections.emptyList();

    private ReportId reportId;

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

    // Value objects for strong typing
    public record InterviewId(Long value) implements Serializable {
    }

    public record UserId(Long value) implements Serializable {
    }

    public record InterviewTemplateId(Long value) implements Serializable {
    }

    public record ReportId(Long value) implements Serializable {
    }
}
