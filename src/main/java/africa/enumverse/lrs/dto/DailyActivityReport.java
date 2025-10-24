package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyActivityReport {
    private LocalDate date;
    private Long totalStatements;
    private Long uniqueActors;
    private Long uniqueActivities;
    private Long completions;
    private Double averageScore;
}

