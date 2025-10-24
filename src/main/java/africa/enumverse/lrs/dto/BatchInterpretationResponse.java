package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchInterpretationResponse {
    private int totalEvents;
    private int successCount;
    private int failureCount;
    private List<InterpretationResponse> responses;
}

