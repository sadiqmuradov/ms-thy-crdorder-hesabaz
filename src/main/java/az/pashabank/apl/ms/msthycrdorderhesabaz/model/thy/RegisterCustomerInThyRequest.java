package az.pashabank.apl.ms.msthycrdorderhesabaz.model.thy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCustomerInThyRequest {

    private String name;
    private String surname;
    private String nationality;
    private String birthDate;
    private String email;
    private String mobileNo;
    private String securityQuestion;
    private String securityAnswer;
    private String password;
    private String repeatPassword;
    private String corrspondanceLanguage;
    private String gender;
    private String countryCode;
    private String cityCode;
    private String address;
}
