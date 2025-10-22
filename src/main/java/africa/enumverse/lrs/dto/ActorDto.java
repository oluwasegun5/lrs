package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorDto {
    private String id; // UUID
    private String name;
    private String mbox; // mailto: URI
    private String mboxSha1sum;
    private String openId;
    private AccountDto account;
    private String objectType; // Agent | Group
}

