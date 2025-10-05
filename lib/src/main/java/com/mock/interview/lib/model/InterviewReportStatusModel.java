package com.mock.interview.lib.model;

import com.mock.interview.lib.contract.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

@With
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
public class InterviewReportStatusModel extends AbstractModel {

    private Long reportId;

    private ReportStatus reportStatus;
}
