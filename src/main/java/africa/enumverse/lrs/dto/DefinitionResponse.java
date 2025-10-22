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
public class DefinitionResponse {
    private Map<String, String> name;
    private Map<String, String> description;
    private String type;
    private String moreInfo;
    private Map<String, Object> extensions;
}

