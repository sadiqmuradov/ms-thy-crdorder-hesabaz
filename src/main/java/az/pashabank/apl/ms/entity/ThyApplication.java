package az.pashabank.apl.ms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Data
@Entity
@Table(name = "thy_applications_hesabaz")
public class ThyApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq_thy_applications_hesabaz")
    @SequenceGenerator(sequenceName = "seq_thy_applications_hesabaz", allocationSize = 1, name = "seq_thy_applications_hesabaz")
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
    private String domicileAddress;
    private String mobileNumber;
    private String email;
    private String secretCode;
    private String workplace;
    private String position;
    private boolean urgent;
    private String tkNo;
    private String passportName;
    private String passportSurname;
    private boolean acceptedTerms;
    private boolean acceptedGsa;
    private String currency;
    private int cardProductId;
    private int period;
    private String branchCode;
    private String branchName;
    private int amountToPay;
    private boolean mailSent;
//    @Column(columnDefinition = "DATE DEFAULT CURRENT_DATE")
    @CreationTimestamp
    private Date createDate;
//    @Column(columnDefinition = "DATE DEFAULT CURRENT_DATE")
    @UpdateTimestamp
    private Date lastUpdate;
    private boolean active;
    private boolean paymentCompleted;
    private String paymentMethod;
    private int couponId;
    private String roamingNumber;

    @Transient
    private boolean domicileSame;
    @Transient
    private boolean tkNoAvailable;
    @Transient
    private String password;
    @Transient
    private String passwordRepeat;
    @Transient
    private MultipartFile[] uploads;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "app", fetch = FetchType.LAZY)
    private List<UploadWrapper> uploadWrappers;

    @Transient
    protected int[] anketAnswers;
    @Transient
    protected String[] anketDescs;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "app", fetch = FetchType.LAZY)
    private List<CRSAnswer> crsAnswers;
    private int step;

    public ThyApplication() {
        this.anketAnswers = new int[100];
        this.anketDescs = new String[100];
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ThyApplication.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("residency='" + residency + "'")
                .add("nationality='" + nationality + "'")
                .add("name='" + name + "'")
                .add("surname='" + surname + "'")
                .add("patronymic='" + patronymic + "'")
                .add("gender='" + gender + "'")
                .add("birthDate='" + birthDate + "'")
                .add("registrationCity='" + registrationCity + "'")
                .add("registrationAddress='" + registrationAddress + "'")
                .add("domicileAddress='" + domicileAddress + "'")
                .add("mobileNumber='" + mobileNumber + "'")
                .add("email='" + email + "'")
                .add("secretCode='" + secretCode + "'")
                .add("workplace='" + workplace + "'")
                .add("position='" + position + "'")
                .add("urgent=" + urgent)
                .add("tkNo='" + tkNo + "'")
                .add("passportName='" + passportName + "'")
                .add("passportSurname='" + passportSurname + "'")
                .add("acceptedTerms=" + acceptedTerms)
                .add("acceptedGsa=" + acceptedGsa)
                .add("currency='" + currency + "'")
                .add("cardProductId=" + cardProductId)
                .add("period=" + period)
                .add("branchCode='" + branchCode + "'")
                .add("branchName='" + branchName + "'")
                .add("amountToPay=" + amountToPay)
                .add("mailSent=" + mailSent)
                .add("createDate=" + createDate)
                .add("lastUpdate=" + lastUpdate)
                .add("active=" + active)
                .add("paymentCompleted=" + paymentCompleted)
                .add("paymentMethod='" + paymentMethod + "'")
                .add("roamingNumber='" + roamingNumber + "'")
                .add("domicileSame=" + domicileSame)
                .add("tkNoAvailable=" + tkNoAvailable)
                .add("password='" + password + "'")
                .add("passwordRepeat='" + passwordRepeat + "'")
                .add("anketAnswers=" + Arrays.toString(anketAnswers))
                .add("anketDescs=" + Arrays.toString(anketDescs))
                .add("step=" + step)
                .toString();
    }
}
