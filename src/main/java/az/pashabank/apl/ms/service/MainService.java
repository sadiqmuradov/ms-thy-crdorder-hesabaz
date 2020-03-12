package az.pashabank.apl.ms.service;

import az.pashabank.apl.ms.entity.Branch;
import az.pashabank.apl.ms.entity.CRSQuestion;
import az.pashabank.apl.ms.entity.CardPrice;
import az.pashabank.apl.ms.entity.CardProduct;
import az.pashabank.apl.ms.entity.City;
import az.pashabank.apl.ms.entity.Country;
import az.pashabank.apl.ms.entity.NetGrossIncomeEntity;
import az.pashabank.apl.ms.entity.SourceOfIncomeEntity;
import az.pashabank.apl.ms.entity.ThyApplication;
import az.pashabank.apl.ms.model.OperationResponse;
import az.pashabank.apl.ms.model.Payment;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Locale;

public interface MainService {

    ThyApplication saveApplication(ThyApplication thyApplication);
    List<Country> getCountryList(String lang);
    Country getCountryByLangAndCode(String lang, String code);
    List<City> getCityList(String countryCode);
    City getCityByCountryCodeAndCode(String countryCode, String code);
    List<CRSQuestion> getCRSQuestionList(String lang);
    List<NetGrossIncomeEntity> getNetGrossIncomesByLang(String lang);
    List<SourceOfIncomeEntity> getSourcesOfIncomeByLang(String lang);
    CardProduct getCardProductById(int id);
    CardPrice getCardPriceByCardProductId(int cardProductId);
    List<CardProduct> getCardProductList();
    List<Branch> getBranchList(String lang);
    Branch getBranchByCodeAndLang(String branchCode, String lang);
    Payment getPaymentByEcommTransId(String ecommTransId);
    Payment getPaymentByAppId(int appId);
    Payment getPaymentByAppIdAndStatus(int appId, int status);
    String registerPaymentAndGetView(ThyApplication app, Locale locale, BindingResult result);
    OperationResponse<String> checkPaymentStatus(String ecommTransId, String ipAddress, String lang);


}
