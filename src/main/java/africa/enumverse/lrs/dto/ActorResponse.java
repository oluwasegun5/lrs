package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorResponse {
    private String id;
    private String name;
    private String mbox;
    private String mboxSha1sum;
    private String openId;
    private AccountResponse account;
    private String objectType; // Agent | Group
}

