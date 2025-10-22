package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementRequest {

    private ActorDto actor;
    private VerbDto verb;
    private ActivityDto object;
    private ContextDto context;
    private ResultDto result;
}
