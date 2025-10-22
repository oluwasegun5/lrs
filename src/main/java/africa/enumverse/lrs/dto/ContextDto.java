package africa.enumverse.lrs.dto;

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
public class ContextDto {
    private String registration;
    private String instructorId;
    private String teamId;
    private Map<String, List<ActivityDto>> contextActivities;
    private String revision;
    private String platform;
    private String language;
    private String statement; // statement id
    private Map<String, Object> extensions;
}

