/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.pashabank.apl.ms.dao;

import az.pashabank.apl.ms.model.OperationResponse;
import az.pashabank.apl.ms.model.Payment;

import java.util.List;

public interface IMainDao {

    String registerPayment(Payment payment);
    void updatePaymentStatus(Payment payment, boolean isSuccessful, String description);
    boolean markApplicationAsPaid(int appId);
    boolean markApplicationAsSent(int appId);
    String getValueFromEmailConfig(String key);
    List<String> getActiveMails();
    OperationResponse<String> makePaymentToFlex(int paymentId);

}
