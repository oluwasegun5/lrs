package africa.enumverse.lrs.service;

import africa.enumverse.lrs.dto.*;
import africa.enumverse.lrs.model.Statement;
import africa.enumverse.lrs.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final StatementRepository statementRepository;

    /**
     * Generate a comprehensive report for a given date range
     */
    public ComprehensiveReport generateComprehensiveReport(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Generating comprehensive report from {} to {}", startDate, endDate);

        List<Statement> statements = statementRepository.findByTimestampBetween(startDate, endDate);

        if (statements.isEmpty()) {
            return ComprehensiveReport.builder()
                    .reportGeneratedAt(LocalDateTime.now())
                    .reportStartDate(startDate)
                    .reportEndDate(endDate)
                    .totalStatements(0L)
                    .totalActors(0L)
                    .totalActivities(0L)
                    .totalVerbs(0L)
                    .verbBreakdown(Collections.emptyList())
                    .topPerformers(Collections.emptyList())
                    .mostPopularActivities(Collections.emptyList())
                    .dailyTrends(Collections.emptyList())
                    .build();
        }

        return ComprehensiveReport.builder()
                .reportGeneratedAt(LocalDateTime.now())
                .reportStartDate(startDate)
                .reportEndDate(endDate)
                .totalStatements((long) statements.size())
                .totalActors(countUniqueActors(statements))
                .totalActivities(countUniqueActivities(statements))
                .totalVerbs(countUniqueVerbs(statements))
                .overallAverageScore(calculateOverallAverageScore(statements))
                .overallCompletionRate(calculateCompletionRate(statements))
                .overallSuccessRate(calculateSuccessRate(statements))
                .verbBreakdown(generateVerbBreakdown(statements))
                .topPerformers(generateTopPerformers(statements, 10))
                .mostPopularActivities(generateMostPopularActivities(statements, 10))
                .dailyTrends(generateDailyTrends(statements))
                .build();
    }

    /**
     * Generate activity-specific report
     */
    public ActivityReport generateActivityReport(String activityId) {
        log.info("Generating activity report for activity: {}", activityId);

        List<Statement> statements = statementRepository.findAll().stream()
                .filter(s -> s.getObject() != null && activityId.equals(s.getObject().getId()))
                .collect(Collectors.toList());

        if (statements.isEmpty()) {
            return ActivityReport.builder()
                    .activityId(activityId)
                    .totalStatements(0L)
                    .build();
        }

        long completedCount = statements.stream()
                .filter(s -> s.getResult() != null && Boolean.TRUE.equals(s.getResult().getCompletion()))
                .count();

        long successCount = statements.stream()
                .filter(s -> s.getResult() != null && Boolean.TRUE.equals(s.getResult().getSuccess()))
                .count();

        double averageScore = statements.stream()
                .filter(s -> s.getResult() != null && s.getResult().getScore() != null && s.getResult().getScore().getScaled() != null)
                .mapToDouble(s -> s.getResult().getScore().getScaled())
                .average()
                .orElse(0.0);

        String activityName = statements.stream()
                .filter(s -> s.getObject() != null && s.getObject().getDefinition() != null && s.getObject().getDefinition().getName() != null)
                .findFirst()
                .map(s -> s.getObject().getDefinition().getName().getOrDefault("en-US", activityId))
                .orElse(activityId);

        return ActivityReport.builder()
                .activityId(activityId)
                .activityName(activityName)
                .totalStatements((long) statements.size())
                .completedCount(completedCount)
                .successCount(successCount)
                .averageScore(averageScore)
                .completionRate(statements.size() > 0 ? (completedCount * 100.0 / statements.size()) : 0.0)
                .successRate(statements.size() > 0 ? (successCount * 100.0 / statements.size()) : 0.0)
                .firstAttempt(statements.stream().map(Statement::getTimestamp).min(LocalDateTime::compareTo).orElse(null))
                .lastAttempt(statements.stream().map(Statement::getTimestamp).max(LocalDateTime::compareTo).orElse(null))
                .build();
    }

    /**
     * Generate actor-specific report
     */
    public ActorReport generateActorReport(String actorId) {
        log.info("Generating actor report for actor: {}", actorId);

        List<Statement> statements = statementRepository.findAll().stream()
                .filter(s -> s.getActor() != null && actorId.equals(s.getActor().getId()))
                .collect(Collectors.toList());

        if (statements.isEmpty()) {
            return ActorReport.builder()
                    .actorId(actorId)
                    .totalStatements(0L)
                    .build();
        }

        long completed = statements.stream()
                .filter(s -> s.getResult() != null && Boolean.TRUE.equals(s.getResult().getCompletion()))
                .count();

        long uniqueActivities = statements.stream()
                .filter(s -> s.getObject() != null && s.getObject().getId() != null)
                .map(s -> s.getObject().getId())
                .distinct()
                .count();

        double averageScore = statements.stream()
                .filter(s -> s.getResult() != null && s.getResult().getScore() != null && s.getResult().getScore().getScaled() != null)
                .mapToDouble(s -> s.getResult().getScore().getScaled())
                .average()
                .orElse(0.0);

        Statement firstStatement = statements.stream().findFirst().orElse(null);
        String actorName = firstStatement != null && firstStatement.getActor() != null ?
                firstStatement.getActor().getName() : actorId;
        String actorEmail = firstStatement != null && firstStatement.getActor() != null ?
                firstStatement.getActor().getMbox() : null;

        return ActorReport.builder()
                .actorId(actorId)
                .actorName(actorName)
                .actorEmail(actorEmail)
                .totalStatements((long) statements.size())
                .activitiesCompleted(completed)
                .activitiesAttempted(uniqueActivities)
                .averageScore(averageScore)
                .completionRate(uniqueActivities > 0 ? (completed * 100.0 / uniqueActivities) : 0.0)
                .firstActivity(statements.stream().map(Statement::getTimestamp).min(LocalDateTime::compareTo).orElse(null))
                .lastActivity(statements.stream().map(Statement::getTimestamp).max(LocalDateTime::compareTo).orElse(null))
                .build();
    }

    /**
     * Generate verb usage breakdown
     */
    public List<VerbReport> generateVerbBreakdown(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Generating verb breakdown from {} to {}", startDate, endDate);

        List<Statement> statements = statementRepository.findByTimestampBetween(startDate, endDate);
        return generateVerbBreakdown(statements);
    }

    /**
     * Generate daily activity trends
     */
    public List<DailyActivityReport> generateDailyTrends(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Generating daily trends from {} to {}", startDate, endDate);

        List<Statement> statements = statementRepository.findByTimestampBetween(startDate, endDate);
        return generateDailyTrends(statements);
    }

    /**
     * Get top performing actors
     */
    public List<ActorReport> getTopPerformers(int limit) {
        log.info("Getting top {} performers", limit);

        List<Statement> allStatements = statementRepository.findAll();
        return generateTopPerformers(allStatements, limit);
    }

    /**
     * Get most popular activities
     */
    public List<ActivityReport> getMostPopularActivities(int limit) {
        log.info("Getting top {} most popular activities", limit);

        List<Statement> allStatements = statementRepository.findAll();
        return generateMostPopularActivities(allStatements, limit);
    }

    // Private helper methods

    private Long countUniqueActors(List<Statement> statements) {
        return statements.stream()
                .filter(s -> s.getActor() != null && s.getActor().getId() != null)
                .map(s -> s.getActor().getId())
                .distinct()
                .count();
    }

    private Long countUniqueActivities(List<Statement> statements) {
        return statements.stream()
                .filter(s -> s.getObject() != null && s.getObject().getId() != null)
                .map(s -> s.getObject().getId())
                .distinct()
                .count();
    }

    private Long countUniqueVerbs(List<Statement> statements) {
        return statements.stream()
                .filter(s -> s.getVerb() != null && s.getVerb().getId() != null)
                .map(s -> s.getVerb().getId())
                .distinct()
                .count();
    }

    private Double calculateOverallAverageScore(List<Statement> statements) {
        return statements.stream()
                .filter(s -> s.getResult() != null && s.getResult().getScore() != null && s.getResult().getScore().getScaled() != null)
                .mapToDouble(s -> s.getResult().getScore().getScaled())
                .average()
                .orElse(0.0);
    }

    private Double calculateCompletionRate(List<Statement> statements) {
        long total = statements.size();
        if (total == 0) return 0.0;

        long completed = statements.stream()
                .filter(s -> s.getResult() != null && Boolean.TRUE.equals(s.getResult().getCompletion()))
                .count();

        return (completed * 100.0) / total;
    }

    private Double calculateSuccessRate(List<Statement> statements) {
        long total = statements.size();
        if (total == 0) return 0.0;

        long success = statements.stream()
                .filter(s -> s.getResult() != null && Boolean.TRUE.equals(s.getResult().getSuccess()))
                .count();

        return (success * 100.0) / total;
    }

    private List<VerbReport> generateVerbBreakdown(List<Statement> statements) {
        long total = statements.size();
        if (total == 0) return Collections.emptyList();

        Map<String, Long> verbCounts = statements.stream()
                .filter(s -> s.getVerb() != null && s.getVerb().getId() != null)
                .collect(Collectors.groupingBy(
                        s -> s.getVerb().getId(),
                        Collectors.counting()
                ));

        Map<String, String> verbDisplays = statements.stream()
                .filter(s -> s.getVerb() != null && s.getVerb().getId() != null)
                .collect(Collectors.toMap(
                        s -> s.getVerb().getId(),
                        s -> s.getVerb().getDisplay() != null ?
                                s.getVerb().getDisplay().getOrDefault("en-US", s.getVerb().getId()) :
                                s.getVerb().getId(),
                        (v1, v2) -> v1
                ));

        return verbCounts.entrySet().stream()
                .map(entry -> VerbReport.builder()
                        .verbId(entry.getKey())
                        .verbDisplay(verbDisplays.getOrDefault(entry.getKey(), entry.getKey()))
                        .count(entry.getValue())
                        .percentage((entry.getValue() * 100.0) / total)
                        .build())
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .collect(Collectors.toList());
    }

    private List<ActorReport> generateTopPerformers(List<Statement> statements, int limit) {
        Map<String, List<Statement>> statementsByActor = statements.stream()
                .filter(s -> s.getActor() != null && s.getActor().getId() != null)
                .collect(Collectors.groupingBy(s -> s.getActor().getId()));

        return statementsByActor.entrySet().stream()
                .map(entry -> {
                    String actorId = entry.getKey();
                    List<Statement> actorStatements = entry.getValue();

                    double avgScore = actorStatements.stream()
                            .filter(s -> s.getResult() != null && s.getResult().getScore() != null && s.getResult().getScore().getScaled() != null)
                            .mapToDouble(s -> s.getResult().getScore().getScaled())
                            .average()
                            .orElse(0.0);

                    long completed = actorStatements.stream()
                            .filter(s -> s.getResult() != null && Boolean.TRUE.equals(s.getResult().getCompletion()))
                            .count();

                    long uniqueActivities = actorStatements.stream()
                            .filter(s -> s.getObject() != null && s.getObject().getId() != null)
                            .map(s -> s.getObject().getId())
                            .distinct()
                            .count();

                    Statement firstStatement = actorStatements.stream().findFirst().orElse(null);
                    String actorName = firstStatement != null && firstStatement.getActor() != null ?
                            firstStatement.getActor().getName() : actorId;
                    String actorEmail = firstStatement != null && firstStatement.getActor() != null ?
                            firstStatement.getActor().getMbox() : null;

                    return ActorReport.builder()
                            .actorId(actorId)
                            .actorName(actorName)
                            .actorEmail(actorEmail)
                            .totalStatements((long) actorStatements.size())
                            .activitiesCompleted(completed)
                            .activitiesAttempted(uniqueActivities)
                            .averageScore(avgScore)
                            .completionRate(uniqueActivities > 0 ? (completed * 100.0 / uniqueActivities) : 0.0)
                            .firstActivity(actorStatements.stream().map(Statement::getTimestamp).min(LocalDateTime::compareTo).orElse(null))
                            .lastActivity(actorStatements.stream().map(Statement::getTimestamp).max(LocalDateTime::compareTo).orElse(null))
                            .build();
                })
                .sorted((a, b) -> Double.compare(b.getAverageScore(), a.getAverageScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<ActivityReport> generateMostPopularActivities(List<Statement> statements, int limit) {
        Map<String, List<Statement>> statementsByActivity = statements.stream()
                .filter(s -> s.getObject() != null && s.getObject().getId() != null)
                .collect(Collectors.groupingBy(s -> s.getObject().getId()));

        return statementsByActivity.entrySet().stream()
                .map(entry -> {
                    String activityId = entry.getKey();
                    List<Statement> activityStatements = entry.getValue();

                    long completedCount = activityStatements.stream()
                            .filter(s -> s.getResult() != null && Boolean.TRUE.equals(s.getResult().getCompletion()))
                            .count();

                    long successCount = activityStatements.stream()
                            .filter(s -> s.getResult() != null && Boolean.TRUE.equals(s.getResult().getSuccess()))
                            .count();

                    double averageScore = activityStatements.stream()
                            .filter(s -> s.getResult() != null && s.getResult().getScore() != null && s.getResult().getScore().getScaled() != null)
                            .mapToDouble(s -> s.getResult().getScore().getScaled())
                            .average()
                            .orElse(0.0);

                    String activityName = activityStatements.stream()
                            .filter(s -> s.getObject() != null && s.getObject().getDefinition() != null && s.getObject().getDefinition().getName() != null)
                            .findFirst()
                            .map(s -> s.getObject().getDefinition().getName().getOrDefault("en-US", activityId))
                            .orElse(activityId);

                    return ActivityReport.builder()
                            .activityId(activityId)
                            .activityName(activityName)
                            .totalStatements((long) activityStatements.size())
                            .completedCount(completedCount)
                            .successCount(successCount)
                            .averageScore(averageScore)
                            .completionRate(activityStatements.size() > 0 ? (completedCount * 100.0 / activityStatements.size()) : 0.0)
                            .successRate(activityStatements.size() > 0 ? (successCount * 100.0 / activityStatements.size()) : 0.0)
                            .firstAttempt(activityStatements.stream().map(Statement::getTimestamp).min(LocalDateTime::compareTo).orElse(null))
                            .lastAttempt(activityStatements.stream().map(Statement::getTimestamp).max(LocalDateTime::compareTo).orElse(null))
                            .build();
                })
                .sorted((a, b) -> Long.compare(b.getTotalStatements(), a.getTotalStatements()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<DailyActivityReport> generateDailyTrends(List<Statement> statements) {
        Map<LocalDate, List<Statement>> statementsByDate = statements.stream()
                .collect(Collectors.groupingBy(s -> s.getTimestamp().toLocalDate()));

        return statementsByDate.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Statement> dailyStatements = entry.getValue();

                    long uniqueActors = dailyStatements.stream()
                            .filter(s -> s.getActor() != null && s.getActor().getId() != null)
                            .map(s -> s.getActor().getId())
                            .distinct()
                            .count();

                    long uniqueActivities = dailyStatements.stream()
                            .filter(s -> s.getObject() != null && s.getObject().getId() != null)
                            .map(s -> s.getObject().getId())
                            .distinct()
                            .count();

                    long completions = dailyStatements.stream()
                            .filter(s -> s.getResult() != null && Boolean.TRUE.equals(s.getResult().getCompletion()))
                            .count();

                    double avgScore = dailyStatements.stream()
                            .filter(s -> s.getResult() != null && s.getResult().getScore() != null && s.getResult().getScore().getScaled() != null)
                            .mapToDouble(s -> s.getResult().getScore().getScaled())
                            .average()
                            .orElse(0.0);

                    return DailyActivityReport.builder()
                            .date(date)
                            .totalStatements((long) dailyStatements.size())
                            .uniqueActors(uniqueActors)
                            .uniqueActivities(uniqueActivities)
                            .completions(completions)
                            .averageScore(avgScore)
                            .build();
                })
                .sorted(Comparator.comparing(DailyActivityReport::getDate))
                .collect(Collectors.toList());
    }
}

