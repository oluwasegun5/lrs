package africa.enumverse.lrs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Actor {
    private String id; // UUID
    private String name;
    private String mbox; // mailto: URI
    private String mboxSha1sum;
    private String openId;
    private Account account;
    private ActorType objectType;
}

