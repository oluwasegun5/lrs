package africa.enumverse.lrs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Score score;
    private Boolean success;
    private Boolean completion;
    private String response;
    private String duration; // ISO8601 duration
    private Map<String, Object> extensions;
}

