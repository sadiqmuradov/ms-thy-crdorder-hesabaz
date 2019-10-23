package az.pashabank.apl.ms.msthycrdorderhesabaz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "THY_APPLICATIONS_HESABAZ")
public class ThyApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "THY_APPLICATIONS_HESABAZ_SEQ")
    @SequenceGenerator(sequenceName = "THY_APPLICATIONS_HESABAZ_SEQ", allocationSize = 1, name = "THY_APPLICATIONS_HESABAZ_SEQ")
    @JsonIgnore
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
