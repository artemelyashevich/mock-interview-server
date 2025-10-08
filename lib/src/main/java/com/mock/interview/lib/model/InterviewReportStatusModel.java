package com.mock.interview.lib.model;

import com.mock.interview.lib.contract.AbstractModel;
import lombok.*;

@With
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
public class InterviewReportStatusModel extends AbstractModel {

    private Long reportId;

    @Setter
    private ReportStatus reportStatus;
}
