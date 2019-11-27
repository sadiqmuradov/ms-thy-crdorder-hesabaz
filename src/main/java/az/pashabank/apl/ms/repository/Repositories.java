package az.pashabank.apl.ms.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Repository
public class Repositories {

    @Autowired
    private ThyApplicationRepo thyApplicationRepo;

    @Autowired
    private CardProductRepo cardProductRepo;

    @Autowired
    private CardPriceRepo cardPriceRepo;

    @Autowired
    private CouponCodeRepo couponCodeRepo;

    @Autowired
    private BranchRepo branchRepo;

    @Autowired
    private CRSQuestionRepo crsQuestionRepo;

    @Autowired
    private CountryRepo countryRepo;

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private UploadWrapperRepo uploadWrapperRepo;

    @Autowired
    private CRSAnswerRepo crsAnswerRepo;

}
