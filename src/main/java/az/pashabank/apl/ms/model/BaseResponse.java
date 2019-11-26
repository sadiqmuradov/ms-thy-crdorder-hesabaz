package az.pashabank.apl.ms.model;

import az.pashabank.apl.ms.enums.BRStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private BRStatus status;
    private String error;
    private Object data;
}
