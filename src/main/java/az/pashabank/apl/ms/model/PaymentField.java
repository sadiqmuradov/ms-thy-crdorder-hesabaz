package az.pashabank.apl.ms.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentField {

    private int paymentId;
    private int id;
    private String value;

    public PaymentField(int paymentId, int id, String value) {
        this.paymentId = paymentId;
        this.id = id;
        this.value = value;
    }

}
