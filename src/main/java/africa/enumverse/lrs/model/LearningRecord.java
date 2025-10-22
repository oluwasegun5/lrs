package africa.enumverse.lrs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "learning_records")
public class LearningRecord {

    @Id
    private String id;

    private String userId;
    private String courseId;
    private String activityType;
    private String activityName;
    private Integer score;
    private Boolean completed;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
    private String status;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}

