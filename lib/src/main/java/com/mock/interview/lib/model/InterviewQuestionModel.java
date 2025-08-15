package com.mock.interview.lib.model;

import com.mock.interview.lib.contract.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.io.Serializable;
import java.util.Objects;

@With
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
public class InterviewQuestionModel extends AbstractModel {

    @EqualsAndHashCode.Include
    private Long id;
    
    @NonNull
    private InterviewId interviewId;
    
    @NonNull
    private String questionText;
    
    private String answerText;
    
    @NonNull
    private String topic;
    
    private EvaluationModel evaluation;
    
    @NonNull
    private Integer orderIndex;

    public void provideAnswer(String answer) {
        this.answerText = Objects.requireNonNull(answer);
    }

    public void evaluate(EvaluationModel evaluation) {
        if (answerText == null) {
            throw new IllegalStateException("Cannot evaluate unanswered question");
        }
        this.evaluation = Objects.requireNonNull(evaluation);
    }

    public boolean isAnswered() {
        return answerText != null && !answerText.isBlank();
    }

    public boolean isEvaluated() {
        return evaluation != null;
    }

    public record InterviewId(Long value) implements Serializable {}
}
