package az.pashabank.apl.ms.msthycrdorderhesabaz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CRSAnswer {
    private Integer appId;
    private Integer questionId;
    private Integer answer;
    private String description;
}
