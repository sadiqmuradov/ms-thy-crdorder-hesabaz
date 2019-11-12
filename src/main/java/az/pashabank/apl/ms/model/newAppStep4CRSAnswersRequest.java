package az.pashabank.apl.ms.model;

import az.pashabank.apl.ms.entity.CRSAnswer;
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
