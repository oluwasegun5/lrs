package africa.enumverse.lrs.controller;

import africa.enumverse.lrs.dto.ApiResponse;
import africa.enumverse.lrs.dto.StatementRequest;
import africa.enumverse.lrs.model.Statement;
import africa.enumverse.lrs.service.StatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/statements")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "xAPI Statements", description = "API endpoints for managing xAPI (Experience API) statements")
public class StatementController {

    private final StatementService statementService;

    @PostMapping
    @Operation(
        summary = "Create a new xAPI statement",
        description = "Creates a new learning activity statement following the xAPI specification"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Statement created successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    public ResponseEntity<ApiResponse<Statement>> createStatement(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "xAPI statement data",
                required = true
            )
            @RequestBody StatementRequest request) {
        log.info("Received request to create statement");
        try {
            Statement statement = statementService.createStatement(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Statement created successfully", statement));
        } catch (Exception e) {
            log.error("Error creating statement", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create statement: " + e.getMessage()));
        }
    }

    @GetMapping
    @Operation(
        summary = "Get all statements",
        description = "Retrieves all xAPI statements from the database"
    )
    public ResponseEntity<ApiResponse<List<Statement>>> getAllStatements() {
        log.info("Received request to get all statements");
        try {
            List<Statement> statements = statementService.getAllStatements();
            return ResponseEntity.ok(ApiResponse.success(statements));
        } catch (Exception e) {
            log.error("Error fetching statements", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch statements: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get statement by ID",
        description = "Retrieves a specific xAPI statement by its unique identifier"
    )
    public ResponseEntity<ApiResponse<Statement>> getStatementById(
            @Parameter(description = "Statement ID", required = true)
            @PathVariable String id) {
        log.info("Received request to get statement by id: {}", id);
        return statementService.getStatementById(id)
                .map(statement -> ResponseEntity.ok(ApiResponse.success(statement)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Statement not found")));
    }

    @GetMapping("/actor/{actorName}")
    @Operation(
        summary = "Get statements by actor name",
        description = "Retrieves all statements for a specific actor (learner)"
    )
    public ResponseEntity<ApiResponse<List<Statement>>> getStatementsByActor(
            @Parameter(description = "Actor/learner name", required = true)
            @PathVariable String actorName) {
        log.info("Received request to get statements by actor: {}", actorName);
        try {
            List<Statement> statements = statementService.getStatementsByActor(actorName);
            return ResponseEntity.ok(ApiResponse.success(statements));
        } catch (Exception e) {
            log.error("Error fetching statements by actor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch statements: " + e.getMessage()));
        }
    }

    @GetMapping("/verb/{verbId}")
    @Operation(
        summary = "Get statements by verb",
        description = "Retrieves all statements with a specific verb (e.g., 'completed', 'passed')"
    )
    public ResponseEntity<ApiResponse<List<Statement>>> getStatementsByVerb(
            @Parameter(description = "Verb ID (e.g., http://adlnet.gov/expapi/verbs/completed)", required = true)
            @PathVariable String verbId) {
        log.info("Received request to get statements by verb: {}", verbId);
        try {
            List<Statement> statements = statementService.getStatementsByVerb(verbId);
            return ResponseEntity.ok(ApiResponse.success(statements));
        } catch (Exception e) {
            log.error("Error fetching statements by verb", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch statements: " + e.getMessage()));
        }
    }

    @GetMapping("/date-range")
    @Operation(
        summary = "Get statements by date range",
        description = "Retrieves statements within a specific time period"
    )
    public ResponseEntity<ApiResponse<List<Statement>>> getStatementsByDateRange(
            @Parameter(description = "Start date and time (ISO format)", required = true, example = "2025-10-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "End date and time (ISO format)", required = true, example = "2025-10-16T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("Received request to get statements between {} and {}", start, end);
        try {
            List<Statement> statements = statementService.getStatementsByDateRange(start, end);
            return ResponseEntity.ok(ApiResponse.success(statements));
        } catch (Exception e) {
            log.error("Error fetching statements by date range", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch statements: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete statement",
        description = "Deletes a specific xAPI statement by its ID"
    )
    public ResponseEntity<ApiResponse<Void>> deleteStatement(
            @Parameter(description = "Statement ID to delete", required = true)
            @PathVariable String id) {
        log.info("Received request to delete statement with id: {}", id);
        try {
            statementService.deleteStatement(id);
            return ResponseEntity.ok(ApiResponse.success("Statement deleted successfully", null));
        } catch (Exception e) {
            log.error("Error deleting statement", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete statement: " + e.getMessage()));
        }
    }
}
