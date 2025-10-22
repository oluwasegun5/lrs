package africa.enumverse.lrs.controller;

import africa.enumverse.lrs.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@Tag(name = "Health Check", description = "API health monitoring endpoint")
public class HealthController {

    @GetMapping("/health")
    @Operation(
        summary = "Check application health",
        description = "Returns the current health status of the LRS application"
    )
    public ResponseEntity<ApiResponse<HealthStatus>> health() {
        HealthStatus status = HealthStatus.builder()
                .status("UP")
                .service("LRS Application")
                .version("1.0.0")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(ApiResponse.success(status));
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthStatus {
        private String status;
        private String service;
        private String version;
        private LocalDateTime timestamp;
    }
}

