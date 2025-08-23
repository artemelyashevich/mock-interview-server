package com.mock.interview.lib.model;

import com.mock.interview.lib.contract.AbstractModel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@With
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ReportModel extends AbstractModel {
    private Long id;

    private String title;

    private String description;

    private ReportFormat format;

    private ReportStatus status;

    private String filePath;

    private LocalDateTime generatedAt;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private Long interviewId;
}