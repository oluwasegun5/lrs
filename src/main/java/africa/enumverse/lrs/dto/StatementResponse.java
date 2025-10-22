package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementResponse {
    private String id;
    private ActorResponse actor;
    private VerbResponse verb;
    private StatementObjectResponse object;
    private LocalDateTime timestamp;
    private LocalDateTime stored;
    private ActorResponse authority;
    private String version;
    private List<Object> attachments;
    private ResultResponse result;
    private ContextResponse context;
}

