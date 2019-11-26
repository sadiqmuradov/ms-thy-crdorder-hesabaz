package az.pashabank.apl.ms.model.thy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileData {

    private String memberId;
    private String dateOfBirthDay;
    private String dateOfBirthMonth;
    private String dateOfBirthYear;
    private String emailAddress;
    private MobilePhone mobilePhone;
    private String name;
    private String surname;
    private String nationality;
    private String pinNumber;
    private String corrspondanceLanguage;
    private String gender;
}
