package africa.enumverse.lrs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Context {
    private String registration; // UUID
    private String instructorId; // FK to Actor (UUID)
    private String teamId; // FK to Actor (UUID)
    private Map<String, List<StatementObject>> contextActivities; // Map<String, List<Object>>
    private String revision;
    private String platform;
    private String language;
    private String statement; // UUID (FK to Statement)
    private Map<String, Object> extensions;
}

