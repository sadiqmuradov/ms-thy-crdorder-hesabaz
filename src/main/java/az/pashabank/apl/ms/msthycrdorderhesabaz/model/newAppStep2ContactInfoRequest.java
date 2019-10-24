package az.pashabank.apl.ms.msthycrdorderhesabaz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class newAppStep2ContactInfoRequest {
    private String registrationCity;
    private String registrationAddress;
    private String mobileNumber;
    private String email;
    private String domicileCity;
    private String domicileAddress;
}
