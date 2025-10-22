package africa.enumverse.lrs.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "statements")
public class Statement {

    @Id
    private String id; // Statement_id (UUID as String)

    private Actor actor; // Actor object (has actor id)
    private Verb verb;   // Verb object
    private StatementObject object; // "object" of the statement
    private LocalDateTime timestamp;
    private LocalDateTime stored;
    private Actor authority; // optional
    private String version;
    private List<Object> attachments; // optional

    private Result result;
    private Context context;
}
