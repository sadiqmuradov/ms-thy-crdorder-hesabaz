package az.pashabank.apl.ms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class newAppStep1GeneralInfoRequest {
    private String name;
    private String surname;
    private String patronymic;
    private String birthDate;
    private String residency;
    private String nationality;
    private String gender;
    private String secretCode;
    private String workplace;
    private String position;
}
