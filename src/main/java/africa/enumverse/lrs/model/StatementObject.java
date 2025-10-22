package africa.enumverse.lrs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementObject {
    private String id; // URI
    private ObjectType objectType;
    private Definition definition;
}

