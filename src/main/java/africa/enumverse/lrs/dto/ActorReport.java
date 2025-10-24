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
public class ActorReport {
    private String actorId;
    private String actorName;
    private String actorEmail;
    private Long totalStatements;
    private Long activitiesCompleted;
    private Long activitiesAttempted;
    private Double averageScore;
    private Double completionRate;
    private String totalTimeSpent;
    private LocalDateTime firstActivity;
    private LocalDateTime lastActivity;
}

