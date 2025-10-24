package africa.enumverse.lrs.controller;

import africa.enumverse.lrs.dto.*;
import africa.enumverse.lrs.service.EventPublisherService;
import africa.enumverse.lrs.service.InterpretationService;
import africa.enumverse.lrs.service.StatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Interpretation Layer API - Accepts simplified learning events from frontend
 * and converts them to xAPI statements
 */
@RestController
@RequestMapping("/api/learning-events")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Learning Events Interpretation", description = "Simplified API for frontend applications to send learning events")
public class InterpretationController {

    private final InterpretationService interpretationService;
    private final StatementService statementService;
    private final EventPublisherService eventPublisherService;

    @PostMapping
    @Operation(
        summary = "Submit a learning event",
        description = "Submit a simplified learning event that will be converted to an xAPI statement, validated, and stored in the LRS"
    )
    public ResponseEntity<ApiResponse<InterpretationResponse>> submitLearningEvent(
            @RequestBody SimplifiedLearningEvent event) {

        log.info("Received learning event: {} - {} - {}",
                event.getLearnerName(), event.getAction(), event.getActivityName());

        try {
            // Step 1: Validate the simplified event
            if (!interpretationService.validateLearningEvent(event)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid learning event: missing required fields"));
            }

            // Step 2: Interpret and convert to xAPI statement
            StatementRequest statementRequest = interpretationService.interpretLearningEvent(event);

            // Step 3: Send to LRS service (which validates and saves)
            StatementResponse statementResponse = statementService.createStatement(statementRequest);

            // Step 4: Publish event to other services
            eventPublisherService.publishStatementCreated(statementResponse);

            // Step 5: Return response
            InterpretationResponse response = InterpretationResponse.builder()
                    .success(true)
                    .message("Learning event processed successfully")
                    .statementId(statementResponse.getId())
                    .timestamp(LocalDateTime.now())
                    .validatedStatement(statementResponse)
                    .build();

            log.info("Learning event processed successfully: {}", statementResponse.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Learning event recorded successfully", response));

        } catch (Exception e) {
            log.error("Error processing learning event", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to process learning event: " + e.getMessage()));
        }
    }

    @PostMapping("/batch")
    @Operation(
        summary = "Submit multiple learning events",
        description = "Submit multiple simplified learning events in a single request for batch processing"
    )
    public ResponseEntity<ApiResponse<BatchInterpretationResponse>> submitBatchLearningEvents(
            @RequestBody BatchLearningEventsRequest request) {

        log.info("Received batch learning events: {} events", request.getEvents().size());

        int successCount = 0;
        int failureCount = 0;
        java.util.List<InterpretationResponse> responses = new java.util.ArrayList<>();

        for (SimplifiedLearningEvent event : request.getEvents()) {
            try {
                // Validate and process each event
                if (!interpretationService.validateLearningEvent(event)) {
                    failureCount++;
                    responses.add(InterpretationResponse.builder()
                            .success(false)
                            .message("Validation failed")
                            .build());
                    continue;
                }

                StatementRequest statementRequest = interpretationService.interpretLearningEvent(event);
                StatementResponse statementResponse = statementService.createStatement(statementRequest);
                eventPublisherService.publishStatementCreated(statementResponse);

                successCount++;
                responses.add(InterpretationResponse.builder()
                        .success(true)
                        .message("Success")
                        .statementId(statementResponse.getId())
                        .timestamp(LocalDateTime.now())
                        .build());

            } catch (Exception e) {
                log.error("Error processing event in batch", e);
                failureCount++;
                responses.add(InterpretationResponse.builder()
                        .success(false)
                        .message("Processing error: " + e.getMessage())
                        .build());
            }
        }

        BatchInterpretationResponse batchResponse = BatchInterpretationResponse.builder()
                .totalEvents(request.getEvents().size())
                .successCount(successCount)
                .failureCount(failureCount)
                .responses(responses)
                .build();

        log.info("Batch processing complete: {} success, {} failures", successCount, failureCount);
        return ResponseEntity.ok(ApiResponse.success("Batch processed", batchResponse));
    }
}

