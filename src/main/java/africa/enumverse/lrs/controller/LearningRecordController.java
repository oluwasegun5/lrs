package africa.enumverse.lrs.controller;

import africa.enumverse.lrs.dto.ApiResponse;
import africa.enumverse.lrs.dto.LearningRecordRequest;
import africa.enumverse.lrs.model.LearningRecord;
import africa.enumverse.lrs.service.LearningRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning-records")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Learning Records", description = "API endpoints for managing learning activity records")
public class LearningRecordController {

    private final LearningRecordService learningRecordService;

    @PostMapping
    @Operation(
        summary = "Create a new learning record",
        description = "Creates a new learning activity record for a user and course"
    )
    public ResponseEntity<ApiResponse<LearningRecord>> createLearningRecord(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Learning record data",
                required = true
            )
            @RequestBody LearningRecordRequest request) {
        log.info("Received request to create learning record");
        try {
            LearningRecord record = learningRecordService.createLearningRecord(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Learning record created successfully", record));
        } catch (Exception e) {
            log.error("Error creating learning record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create learning record: " + e.getMessage()));
        }
    }

    @GetMapping
    @Operation(
        summary = "Get all learning records",
        description = "Retrieves all learning records from the database"
    )
    public ResponseEntity<ApiResponse<List<LearningRecord>>> getAllLearningRecords() {
        log.info("Received request to get all learning records");
        try {
            List<LearningRecord> records = learningRecordService.getAllLearningRecords();
            return ResponseEntity.ok(ApiResponse.success(records));
        } catch (Exception e) {
            log.error("Error fetching learning records", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch learning records: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get learning record by ID",
        description = "Retrieves a specific learning record by its unique identifier"
    )
    public ResponseEntity<ApiResponse<LearningRecord>> getLearningRecordById(
            @Parameter(description = "Learning record ID", required = true)
            @PathVariable String id) {
        log.info("Received request to get learning record by id: {}", id);
        return learningRecordService.getLearningRecordById(id)
                .map(record -> ResponseEntity.ok(ApiResponse.success(record)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Learning record not found")));
    }

    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Get learning records by user ID",
        description = "Retrieves all learning records for a specific user"
    )
    public ResponseEntity<ApiResponse<List<LearningRecord>>> getLearningRecordsByUserId(
            @Parameter(description = "User ID", required = true, example = "user123")
            @PathVariable String userId) {
        log.info("Received request to get learning records for user: {}", userId);
        try {
            List<LearningRecord> records = learningRecordService.getLearningRecordsByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success(records));
        } catch (Exception e) {
            log.error("Error fetching learning records by user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch learning records: " + e.getMessage()));
        }
    }

    @GetMapping("/course/{courseId}")
    @Operation(
        summary = "Get learning records by course ID",
        description = "Retrieves all learning records for a specific course"
    )
    public ResponseEntity<ApiResponse<List<LearningRecord>>> getLearningRecordsByCourseId(
            @Parameter(description = "Course ID", required = true, example = "course456")
            @PathVariable String courseId) {
        log.info("Received request to get learning records for course: {}", courseId);
        try {
            List<LearningRecord> records = learningRecordService.getLearningRecordsByCourseId(courseId);
            return ResponseEntity.ok(ApiResponse.success(records));
        } catch (Exception e) {
            log.error("Error fetching learning records by course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch learning records: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}/course/{courseId}")
    @Operation(
        summary = "Get learning records by user and course",
        description = "Retrieves all learning records for a specific user in a specific course"
    )
    public ResponseEntity<ApiResponse<List<LearningRecord>>> getLearningRecordsByUserAndCourse(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId,
            @Parameter(description = "Course ID", required = true)
            @PathVariable String courseId) {
        log.info("Received request to get learning records for user: {} and course: {}", userId, courseId);
        try {
            List<LearningRecord> records = learningRecordService.getLearningRecordsByUserAndCourse(userId, courseId);
            return ResponseEntity.ok(ApiResponse.success(records));
        } catch (Exception e) {
            log.error("Error fetching learning records", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch learning records: " + e.getMessage()));
        }
    }

    @GetMapping("/completed/{completed}")
    @Operation(
        summary = "Get completed or incomplete records",
        description = "Retrieves learning records based on completion status"
    )
    public ResponseEntity<ApiResponse<List<LearningRecord>>> getCompletedRecords(
            @Parameter(description = "Completion status (true for completed, false for incomplete)", required = true)
            @PathVariable Boolean completed) {
        log.info("Received request to get completed learning records: {}", completed);
        try {
            List<LearningRecord> records = learningRecordService.getCompletedRecords(completed);
            return ResponseEntity.ok(ApiResponse.success(records));
        } catch (Exception e) {
            log.error("Error fetching completed learning records", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch learning records: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update learning record",
        description = "Updates an existing learning record"
    )
    public ResponseEntity<ApiResponse<LearningRecord>> updateLearningRecord(
            @Parameter(description = "Learning record ID", required = true)
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated learning record data",
                required = true
            )
            @RequestBody LearningRecordRequest request) {
        log.info("Received request to update learning record with id: {}", id);
        try {
            LearningRecord record = learningRecordService.updateLearningRecord(id, request);
            return ResponseEntity.ok(ApiResponse.success("Learning record updated successfully", record));
        } catch (Exception e) {
            log.error("Error updating learning record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update learning record: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete learning record",
        description = "Deletes a specific learning record by its ID"
    )
    public ResponseEntity<ApiResponse<Void>> deleteLearningRecord(
            @Parameter(description = "Learning record ID to delete", required = true)
            @PathVariable String id) {
        log.info("Received request to delete learning record with id: {}", id);
        try {
            learningRecordService.deleteLearningRecord(id);
            return ResponseEntity.ok(ApiResponse.success("Learning record deleted successfully", null));
        } catch (Exception e) {
            log.error("Error deleting learning record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete learning record: " + e.getMessage()));
        }
    }
}
