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
public class Definition {
    private Map<String, String> name; // language map
    private Map<String, String> description; // language map
    private String type; // URI
    private String moreInfo; // URI
    private Map<String, Object> extensions; // optional
}

