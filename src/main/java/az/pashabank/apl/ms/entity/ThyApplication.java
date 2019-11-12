package az.pashabank.apl.ms.entity;

import az.pashabank.apl.ms.model.UploadWrapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.List;

@Data
@Entity
@Table(name = "THY_APPLICATIONS_HESABAZ")
public class ThyApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_THY_APPLICATIONS_HESABAZ")
    @SequenceGenerator(sequenceName = "SEQ_THY_APPLICATIONS_HESABAZ", allocationSize = 1, name = "SEQ_THY_APPLICATIONS_HESABAZ")
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
    private String urgent;
    private String tkNo;
    private String passportName;
    private String passportSurname;
    private boolean acceptedTerms;
    private boolean acceptedGsa;
    private String currency;
    private Integer cardProductId;
    private Integer period;
    private String branchCode;
    private String branchName;
    private Integer amountToPay;
    private int mailSent;
    @Transient
    private Date createDate;
    @Transient
    private Date lastUpdate;
    private int active;
    private int paymentCompleted;
    private String paymentMethod;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "app")
//    @JoinColumn(name = "app_id", referencedColumnName = "app_id")
    private List<UploadWrapper> uploadWrappers;

    @Transient
    protected Integer[] anketAnswers;
    @Transient
    protected String[] anketDescs;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "app")
//    @JoinColumn(name = "app_id", referencedColumnName = "app_id")
    private List<CRSAnswer> crsAnswers;

    public ThyApplication() {
        this.anketAnswers = new Integer[100];
        this.anketDescs = new String[100];
    }

}
