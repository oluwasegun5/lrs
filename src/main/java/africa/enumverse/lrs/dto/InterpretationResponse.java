package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterpretationResponse {
    private boolean success;
    private String message;
    private String statementId;
    private LocalDateTime timestamp;
    private Object validatedStatement; // The full xAPI statement that was created
}

