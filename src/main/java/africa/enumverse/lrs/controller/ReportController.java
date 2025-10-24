package africa.enumverse.lrs.controller;

import africa.enumverse.lrs.dto.*;
import africa.enumverse.lrs.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reports & Analytics", description = "API endpoints for generating reports and analytics from xAPI statements")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/comprehensive")
    @Operation(
        summary = "Generate comprehensive report",
        description = "Generates a comprehensive report including all statistics, trends, and breakdowns for the specified date range"
    )
    public ResponseEntity<ApiResponse<ComprehensiveReport>> getComprehensiveReport(
            @Parameter(description = "Start date and time (ISO format)", required = true, example = "2025-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date and time (ISO format)", required = true, example = "2025-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("Received request for comprehensive report from {} to {}", startDate, endDate);
        try {
            ComprehensiveReport report = reportService.generateComprehensiveReport(startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success("Comprehensive report generated successfully", report));
        } catch (Exception e) {
            log.error("Error generating comprehensive report", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to generate comprehensive report: " + e.getMessage()));
        }
    }

    @GetMapping("/activity/{activityId}")
    @Operation(
        summary = "Get activity report",
        description = "Generates a detailed report for a specific activity including completion rates, scores, and usage statistics"
    )
    public ResponseEntity<ApiResponse<ActivityReport>> getActivityReport(
            @Parameter(description = "Activity ID (URI)", required = true)
            @PathVariable String activityId) {

        log.info("Received request for activity report: {}", activityId);
        try {
            ActivityReport report = reportService.generateActivityReport(activityId);
            return ResponseEntity.ok(ApiResponse.success("Activity report generated successfully", report));
        } catch (Exception e) {
            log.error("Error generating activity report", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to generate activity report: " + e.getMessage()));
        }
    }

    @GetMapping("/actor/{actorId}")
    @Operation(
        summary = "Get actor/learner report",
        description = "Generates a detailed report for a specific actor including their performance, completion rates, and activity history"
    )
    public ResponseEntity<ApiResponse<ActorReport>> getActorReport(
            @Parameter(description = "Actor ID (UUID)", required = true)
            @PathVariable String actorId) {

        log.info("Received request for actor report: {}", actorId);
        try {
            ActorReport report = reportService.generateActorReport(actorId);
            return ResponseEntity.ok(ApiResponse.success("Actor report generated successfully", report));
        } catch (Exception e) {
            log.error("Error generating actor report", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to generate actor report: " + e.getMessage()));
        }
    }

    @GetMapping("/verbs")
    @Operation(
        summary = "Get verb usage breakdown",
        description = "Generates a breakdown of verb usage showing counts and percentages for each verb type"
    )
    public ResponseEntity<ApiResponse<List<VerbReport>>> getVerbBreakdown(
            @Parameter(description = "Start date and time (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date and time (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("Received request for verb breakdown from {} to {}", startDate, endDate);
        try {
            List<VerbReport> reports = reportService.generateVerbBreakdown(startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success("Verb breakdown generated successfully", reports));
        } catch (Exception e) {
            log.error("Error generating verb breakdown", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to generate verb breakdown: " + e.getMessage()));
        }
    }

    @GetMapping("/daily-trends")
    @Operation(
        summary = "Get daily activity trends",
        description = "Generates daily trends showing activity levels, unique users, and performance metrics over time"
    )
    public ResponseEntity<ApiResponse<List<DailyActivityReport>>> getDailyTrends(
            @Parameter(description = "Start date and time (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date and time (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("Received request for daily trends from {} to {}", startDate, endDate);
        try {
            List<DailyActivityReport> reports = reportService.generateDailyTrends(startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success("Daily trends generated successfully", reports));
        } catch (Exception e) {
            log.error("Error generating daily trends", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to generate daily trends: " + e.getMessage()));
        }
    }

    @GetMapping("/top-performers")
    @Operation(
        summary = "Get top performing actors/learners",
        description = "Returns a ranked list of top performing actors based on their average scores and completion rates"
    )
    public ResponseEntity<ApiResponse<List<ActorReport>>> getTopPerformers(
            @Parameter(description = "Number of top performers to return", example = "10")
            @RequestParam(defaultValue = "10") int limit) {

        log.info("Received request for top {} performers", limit);
        try {
            List<ActorReport> reports = reportService.getTopPerformers(limit);
            return ResponseEntity.ok(ApiResponse.success("Top performers retrieved successfully", reports));
        } catch (Exception e) {
            log.error("Error retrieving top performers", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to retrieve top performers: " + e.getMessage()));
        }
    }

    @GetMapping("/popular-activities")
    @Operation(
        summary = "Get most popular activities",
        description = "Returns a ranked list of most popular activities based on the number of statements/interactions"
    )
    public ResponseEntity<ApiResponse<List<ActivityReport>>> getMostPopularActivities(
            @Parameter(description = "Number of activities to return", example = "10")
            @RequestParam(defaultValue = "10") int limit) {

        log.info("Received request for top {} most popular activities", limit);
        try {
            List<ActivityReport> reports = reportService.getMostPopularActivities(limit);
            return ResponseEntity.ok(ApiResponse.success("Most popular activities retrieved successfully", reports));
        } catch (Exception e) {
            log.error("Error retrieving popular activities", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to retrieve popular activities: " + e.getMessage()));
        }
    }
}

