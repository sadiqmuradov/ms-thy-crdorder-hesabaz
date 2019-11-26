/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.pashabank.apl.ms.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentStatus {

    private int id;
    private String status;

    public PaymentStatus(int id) {
        this.id = id;
    }

    public PaymentStatus(int id, String status) {
        this.id = id;
        this.status = status;
    }

}
