package az.pashabank.apl.ms.msthycrdorderhesabaz.model.thy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobilePhone {
    private String phoneCountryCode;
    private String phoneNumber;
}
