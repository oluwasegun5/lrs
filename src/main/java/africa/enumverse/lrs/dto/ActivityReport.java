package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityReport {
    private String activityId;
    private String activityName;
    private Long totalStatements;
    private Long completedCount;
    private Long successCount;
    private Double averageScore;
    private Double completionRate;
    private Double successRate;
    private LocalDateTime firstAttempt;
    private LocalDateTime lastAttempt;
}

