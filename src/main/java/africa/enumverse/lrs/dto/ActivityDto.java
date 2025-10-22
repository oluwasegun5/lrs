package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    private String id; // URI
    private String objectType; // Activity|Agent|Group|SubStatement|StatementRef
    private ActivityDefinitionDto definition;
}

