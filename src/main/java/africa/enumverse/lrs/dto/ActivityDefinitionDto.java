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
public class ActivityDefinitionDto {
    private Map<String, String> name; // language map
    private Map<String, String> description;
    private String type; // URI
    private String moreInfo; // URI
    private Map<String, Object> extensions;
}

