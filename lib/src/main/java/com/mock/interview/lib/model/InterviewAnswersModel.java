package com.mock.interview.lib.model;

import com.mock.interview.lib.contract.AbstractModel;
import lombok.*;

import java.util.List;

@With
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
public class InterviewAnswersModel extends AbstractModel {

    private List<InterviewQuestionModel> questions;
}
