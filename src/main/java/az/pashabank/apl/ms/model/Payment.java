package az.pashabank.apl.ms.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "payments_hesabaz")
public class Payment {

    @Id
    private int id;
    private int clientId;
    private String transactionId;
    private String lang;
    private String ipAddress;
    private String currency;
    @Transient
    private List<PaymentField> fields;
    private int amount;
    private String description;
    private String ecommTrans;
    private String ecommResult;
    private String ecommResultPs;
    private String ecommResultCode;
    private String ecomm3dSecure;
    private String ecommRrn;
    private String ecommApprovalCode;
    private String ecommCardNumber;
    private String ecommAav;
    @Column(name = "payment_status")
    private int status;
    private Date createDate;
    private Date lastUpdate;
    private String paymentDesc;
    private int appId;

    public Payment(int id, String lang, String ipAddress, String currency, int amount, String description, String ecommTransaction) {
        this.id = id;
        this.lang = lang;
        this.ipAddress = ipAddress;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.ecommTrans = ecommTrans;
    }

    public Payment(int clientId, String lang, String ipAddress, String currency, int amount, String description, String ecommTransaction, int appId) {
        this.clientId = clientId;
        this.lang = lang;
        this.ipAddress = ipAddress;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.ecommTrans = ecommTrans;
        this.appId = appId;
    }

}
