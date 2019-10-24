package az.pashabank.apl.ms.msthycrdorderhesabaz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class newAppStep4CRSAnswersRequest {
    private List<CRSAnswer> crsAnswers;
}
