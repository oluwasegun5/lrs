package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse {
    private ScoreResponse score;
    private Boolean success;
    private Boolean completion;
    private String response;
    private String duration;
    private Map<String, Object> extensions;
}

