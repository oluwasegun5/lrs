package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Simplified request from frontend - business-friendly format
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedLearningEvent {

    // Who did it?
    private String learnerId;
    private String learnerName;
    private String learnerEmail;

    // What did they do?
    private String action; // e.g., "completed", "started", "passed", "failed", "viewed"
    private String activityId;
    private String activityName;
    private String activityType; // e.g., "course", "module", "quiz", "video"

    // How did they do?
    private Float score; // 0-100
    private Boolean passed;
    private Boolean completed;
    private String duration; // e.g., "PT1H30M" or "5400" (seconds)

    // Context
    private String platform;
    private String courseId;
    private String courseName;
    private String instructorId;
    private String sessionId;

    // Additional metadata
    private Map<String, Object> metadata;
}

