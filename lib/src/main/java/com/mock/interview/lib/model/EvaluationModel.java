package com.mock.interview.lib.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationModel implements Serializable {
    private Long interviewId;
    private Long questionId;
    private Double score;
    private String feedback;
}
