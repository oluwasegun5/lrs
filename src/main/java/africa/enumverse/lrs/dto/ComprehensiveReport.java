package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComprehensiveReport {
    private LocalDateTime reportGeneratedAt;
    private LocalDateTime reportStartDate;
    private LocalDateTime reportEndDate;

    // Overview statistics
    private Long totalStatements;
    private Long totalActors;
    private Long totalActivities;
    private Long totalVerbs;

    // Performance metrics
    private Double overallAverageScore;
    private Double overallCompletionRate;
    private Double overallSuccessRate;

    // Detailed breakdowns
    private List<VerbReport> verbBreakdown;
    private List<ActorReport> topPerformers;
    private List<ActivityReport> mostPopularActivities;
    private List<DailyActivityReport> dailyTrends;
}

