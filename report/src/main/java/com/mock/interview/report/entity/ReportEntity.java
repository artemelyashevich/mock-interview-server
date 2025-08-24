package com.mock.interview.report.entity;

import com.mock.interview.lib.contract.AbstractEntity;
import com.mock.interview.lib.model.ReportFormat;
import com.mock.interview.lib.model.ReportStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEntity extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportFormat format;

    @Column(nullable = false)
    private String status;
    
    private String filePath;

    private LocalDateTime generatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Long interviewId;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = ReportStatus.PENDING.name();
    }
}