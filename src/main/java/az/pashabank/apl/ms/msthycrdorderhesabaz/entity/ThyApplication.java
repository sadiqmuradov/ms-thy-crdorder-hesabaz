package az.pashabank.apl.ms.msthycrdorderhesabaz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "THY_APPLICATIONS_HESABAZ")
public class ThyApplication {
    private int id;
    private String residency;
    private String nationality;
    private String name;
    private String surname;
    private String patronymic;
    private String gender;
    private String birthDate;
    private String registrationCity;
    private String registrationAddress;
    private String domicileCity;
    private String domicileAddress;
    private String mobileNumber;
    private String email;
    private String secretCode;
    private String workplace;
    private String position;
    private String urgent;
    private String tkNo;
    private String passportName;
    private String passportSurname;
    private int acceptedTerms;
    private int acceptedGsa;
    private String currency;
    private int cardProductId;
    private int period;
    private String branchCode;
    private String branchName;
    private int amountToPay;
    private int mailSent;
    private java.util.Date createDate;
    private java.util.Date lastUpdate;
    private int active;
    private int paymentCompleted;
    private String paymentMethod;
    private String roamingNumber;
}
