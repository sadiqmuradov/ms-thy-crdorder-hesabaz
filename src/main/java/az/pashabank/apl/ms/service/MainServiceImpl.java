package az.pashabank.apl.ms.service;

import az.pashabank.apl.ms.dao.MainDao;
import az.pashabank.apl.ms.entity.Branch;
import az.pashabank.apl.ms.entity.CRSAnswer;
import az.pashabank.apl.ms.entity.CRSQuestion;
import az.pashabank.apl.ms.entity.CardPrice;
import az.pashabank.apl.ms.entity.CardProduct;
import az.pashabank.apl.ms.entity.City;
import az.pashabank.apl.ms.entity.Country;
import az.pashabank.apl.ms.entity.CouponCode;
import az.pashabank.apl.ms.entity.NetGrossIncomeEntity;
import az.pashabank.apl.ms.entity.SourceOfIncomeEntity;
import az.pashabank.apl.ms.entity.ThyApplication;
import az.pashabank.apl.ms.entity.UploadWrapper;
import az.pashabank.apl.ms.enums.ResultCode;
import az.pashabank.apl.ms.logger.MainLogger;
import az.pashabank.apl.ms.model.OperationResponse;
import az.pashabank.apl.ms.model.Payment;
import az.pashabank.apl.ms.model.ecomm.ECommRegisterPaymentRequest;
import az.pashabank.apl.ms.model.ecomm.ECommRegisterPaymentResponse;
import az.pashabank.apl.ms.model.ecomm.EcommPaymentStatusRequest;
import az.pashabank.apl.ms.model.ecomm.EcommPaymentStatusResponse;
import az.pashabank.apl.ms.proxy.ECommServiceProxy;
import az.pashabank.apl.ms.repository.Repositories;
import az.pashabank.apl.ms.utils.Utils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MainServiceImpl implements MainService {

    private static final MainLogger LOGGER = MainLogger.getLogger(MainServiceImpl.class);

    private static final String SUCCESS = "Operation is successful";
    private static final String TRANSACTION_FAILED = "E-comm transaction failed";

    @Autowired
    private Repositories repositories;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    private ECommServiceProxy eCommServiceProxy;

    @Autowired
    private MainDao mainDao;

    @Autowired
    protected MailService mailService;

    @Value("${ecomm.clientId}")
    private int ecommClientId;

    @Value("${ecomm.payment.description}")
    private String paymentDescription;

    public ThyApplication saveApplication(ThyApplication thyApplication) {
        return repositories.getThyApplicationRepo().save(thyApplication);
    }

    public void deleteApplication(int appId) {
        repositories.getThyApplicationRepo().deleteById(appId);
        // delete olmali deyil ancaq update
    }

    public ThyApplication getApplicationById(int appId) {
        return repositories.getThyApplicationRepo().getOne(appId);
    }

    @Transactional(readOnly = true)
    public List<ThyApplication> getUnsentApplications() {
        List<ThyApplication> thyApplications = repositories.getThyApplicationRepo().findAllByPaymentCompletedTrueAndMailSentFalseOrderByCreateDate();
        for (ThyApplication app : thyApplications) {
            Hibernate.initialize(app.getUploadWrappers());
            Hibernate.initialize(app.getCrsAnswers());
        }
        return thyApplications;
    }

    public List<Country> getCountryList(String lang) {
        return repositories.getCountryRepo().findAllByLangIgnoreCaseOrderByName(lang);
    }

    public Country getCountryByLangAndCode(String lang, String code) {
        return repositories.getCountryRepo().findCountryByLangIgnoreCaseAndCode(lang, code);
    }

    public List<City> getCityList(String countryCode) {
        return repositories.getCityRepo().findAllByCountryCodeIgnoreCaseOrderByName(countryCode);
    }

    public City getCityByCountryCodeAndCode(String countryCode, String code) {
        return repositories.getCityRepo().findCityByCountryCodeAndCode(countryCode, code);
    }

    public List<CRSQuestion> getCRSQuestionList(String lang) {
        return repositories.getCrsQuestionRepo().findAllByLangIgnoreCase(lang);
    }

    @Override
    public List<NetGrossIncomeEntity> getNetGrossIncomesByLang(String lang) {
        return repositories.getNetGrossIncomeRepo().findNetGrossIncomeEntitiesByLangIgnoreCaseOrderById(lang);
    }

    @Override
    public List<SourceOfIncomeEntity> getSourcesOfIncomeByLang(String lang) {
        return repositories.getSourceOfIncomeRepo().findSourceOfIncomeEntitiesByLangIgnoreCaseOrderById(lang);
    }

    @Override
    public CardProduct getCardProductById(int id) {
        return repositories.getCardProductRepo().findCardProductByIdAndActiveTrue(id);
    }

    @Override
    public CardPrice getCardPriceByCardProductId(int cardProductId) {
        return repositories.getCardPriceRepo().findCardPriceByCardProductIdAndPeriod(cardProductId, 3);
    }

    @Override
    public List<CardProduct> getCardProductList() {
        List<CardProduct> cardProductList = repositories.getCardProductRepo().findAllByActiveTrueAndCardSaleTrue();
        cardProductList = cardProductList.stream().map(cardProduct -> {
            CardPrice cardPrice = repositories.getCardPriceRepo().findCardPriceByCardProductIdAndPeriod(cardProduct.getId(), 3);
            return new CardProduct(cardProduct.getId(), cardProduct.getName(), cardProduct.getUrgency(), cardPrice.getLcyAmount());
        }).collect(Collectors.toList());
        return cardProductList;
    }

    @Override
    public List<Branch> getBranchList(String lang) {
        return repositories.getBranchRepo().findAllByActiveTrueAndLangIgnoreCaseOrderByOrderby(lang);
    }

    @Override
    public Branch getBranchByCodeAndLang(String branchCode, String lang) {
        return repositories.getBranchRepo().findBranchByCodeAndActiveTrueAndLangIgnoreCase(branchCode, lang);
    }

    @Override
    public Payment getPaymentByEcommTransId(String ecommTransId) {
        return repositories.getPaymentRepo().findPaymentByEcommTransAndStatus(ecommTransId, 1);
    }

    @Override
    public Payment getPaymentByAppIdAndStatus(int appId, int status) {
        return repositories.getPaymentRepo().findPaymentByAppIdAndStatus(appId, status);
    }

    public Payment getPaymentByAppId(int appId) {
        return repositories.getPaymentRepo().findPaymentByAppId(appId);
    }

    public List<Payment> getUnpaidFlexPayments() {
        return repositories.getPaymentRepo().findAllByStatusOrderByCreateDate(2);
    }

    public CouponCode getCouponCodeByAppId(int appId) {
        return repositories.getCouponCodeRepo().findCouponCodeByAppid(appId);
    }

    public List<UploadWrapper> getUploadWrappersByAppId(int appId) {
        return repositories.getUploadWrapperRepo().findAllByAppId(appId);
    }

    public List<CRSAnswer> getCRSAnswersByAppId(int appId) {
        return repositories.getCrsAnswerRepo().findAllByAppId(appId);
    }

    public String registerPaymentAndGetView(ThyApplication app, Locale locale, BindingResult result) {
        String view = null;
        String ipAddress = Utils.getIpAddress(request);
        if (app.isUrgent() || "CARD".equals(app.getPaymentMethod())) {
            String ecommAmount = String.valueOf(app.getAmountToPay() * 100);
            ECommRegisterPaymentRequest eCommRegisterPaymentRequest = new ECommRegisterPaymentRequest(
                    ecommClientId, ecommAmount, "944",
                    ipAddress, paymentDescription, locale.getLanguage()
            );
            ECommRegisterPaymentResponse eCommRegisterPaymentResponse = eCommServiceProxy.registerPayment(eCommRegisterPaymentRequest);
            LOGGER.info("eCommRegisterPaymentResponse: {}", eCommRegisterPaymentResponse);
            String ecommTransId = eCommRegisterPaymentResponse.getTransactionId();
            if (eCommRegisterPaymentResponse.getCode().equals("SUCCESS")) {
                String encodedEcommTransId = "";
                try {
                    encodedEcommTransId = URLEncoder.encode(ecommTransId, StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                registerDBPayment(locale.getLanguage(), ipAddress, app.getAmountToPay(), ecommTransId, app.getId());
                view = "redirect:" + eCommRegisterPaymentResponse.getClientHandlerUrl() + encodedEcommTransId;
            } else {
                result.addError(
                        new ObjectError(
                                "ecomm_register_payment_error",
                                new String[] { "message.errors.ecomm_register_payment_error" },
                                new Object[] { },
                                ""
                        )
                );
            }
        } else {
            registerDBPayment(locale.getLanguage(), ipAddress, app.getAmountToPay(), null, app.getId());
            view = "redirect:/step8";
        }
        return view;
    }

    public void setCouponUsed(ThyApplication app) {
        LOGGER.info("Set coupon app ID and change status to USED. Coupon ID: {}, App ID: {}", app.getCouponId(), app.getId());
        CouponCode couponCode = repositories.getCouponCodeRepo().findById(app.getCouponId()).orElse(null);
        if (couponCode != null) {
            couponCode.setAppid(app.getId());
            couponCode.setStatus(3);
            repositories.getCouponCodeRepo().save(couponCode);
        } else {
            LOGGER.error("setCouponUsed. Updating coupon status. COUPON NOT FOUND. Coupon ID: {}", app.getCouponId());
        }
    }

    private void registerDBPayment(String lang, String ipAddress, int amountToPay, String ecommTransId, int appId) {
        Payment payment = new Payment(
                ecommClientId, lang, ipAddress,"AZN",
                amountToPay, paymentDescription, ecommTransId, appId
        );
        LOGGER.info("registerPayment. payment: {}", payment);
        String paymentTransId = mainDao.registerPayment(payment);
        LOGGER.info("After registerPayment. paymentTransId: {}", paymentTransId);
    }

    public String checkPaymentStatusAndGetView(String ecommTransId, Locale locale, Model model) {
        try {
            if (ecommTransId == null || ecommTransId.trim().isEmpty()) {
                throw new Exception("/ecomm/ok [POST] request parameter ecommTransId is null or empty!");
            }

            LOGGER.info("/ecomm/ok [POST] checkPaymentStatusAndGetView. getPaymentByEcommTransId. ecommTransId: {}", ecommTransId);
            Payment payment = getPaymentByEcommTransId(ecommTransId);
            LOGGER.info("/ecomm/ok [POST] checkPaymentStatusAndGetView. After getting payment. payment: {}", payment);

            if (payment == null) {
                throw new Exception("/ecomm/ok [POST] checkPaymentStatusAndGetView. Payment with ecomm trans. id " +  ecommTransId + " NOT FOUND in database");
            }

            OperationResponse<String> operationResponse = checkPaymentStatus(ecommTransId, payment.getIpAddress(), locale.getLanguage());
            String message = operationResponse.getData();
            Map<String, String> parsedPaymentStatus = processEcommMessage(payment, message);

            if (parsedPaymentStatus.get("result").equalsIgnoreCase("OK")) {
                applyPostSuccessPaymentSteps(payment, true);
                String createDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(payment.getCreateDate());
                model.addAttribute("payment", payment);
                model.addAttribute("createDate", createDate);
                model.addAttribute("operation_code", "ok");
            } else {
                LOGGER.error("/ecomm/ok [POST] Ecomm operation failed: {}", parsedPaymentStatus);
                LOGGER.info("/ecomm/ok [POST] Update payment status in payments db. Payment: {}", payment);
                mainDao.updatePaymentStatus(payment, false, "Ecomm operation error");
                model.addAttribute("operation_code", "error");
                model.addAttribute("result_code", parsedPaymentStatus.get("result_code"));
            }

            return "ecomm_result";
        } catch(Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return "redirect:/index";
    }

    @Override
    public OperationResponse<String> checkPaymentStatus(String ecommTransId, String ipAddress, String lang) {
        OperationResponse<String> operationResponse = new OperationResponse<>(ResultCode.ERROR);

        EcommPaymentStatusRequest ecommPaymentStatusRequest = new EcommPaymentStatusRequest(ecommClientId, ecommTransId, ipAddress);
        EcommPaymentStatusResponse ecommPaymentStatusResponse = eCommServiceProxy.getPaymentStatus(lang, ecommPaymentStatusRequest);
        LOGGER.info("checkPaymentStatus. eCommServiceProxy ecommPaymentStatusRequest: {}, ecommPaymentStatusResponse: {}", ecommPaymentStatusRequest, ecommPaymentStatusResponse);

        if (ecommPaymentStatusResponse.getCode().equals("SUCCESS")) {
            operationResponse.setCode(ResultCode.OK);
            operationResponse.setMessage(SUCCESS);
        } else {
            operationResponse.setMessage(TRANSACTION_FAILED);
        }
        operationResponse.setData(ecommPaymentStatusResponse.getMessage());

        return operationResponse;
    }

    protected Map<String, String> processEcommMessage(Payment payment, String message) {
        String messageAsMapFormat = message.substring(1, message.length() - 1);
        Map<String, String> parsedPaymentStatus = Arrays.stream(messageAsMapFormat.split(","))
                .map(s -> s.split("=", 2))
                .collect(Collectors.toMap(s -> s[0].trim(), s -> s[1].trim()));

        payment.setEcommResult(parsedPaymentStatus.get("result"));
        payment.setEcommResultCode(parsedPaymentStatus.get("result_code"));
        payment.setEcommRrn(parsedPaymentStatus.get("rrn"));
        payment.setEcommApprovalCode(parsedPaymentStatus.get("approval_code"));
        payment.setEcomm3dSecure(parsedPaymentStatus.get("3dsecure"));
        payment.setEcommCardNumber(parsedPaymentStatus.get("card_number"));

        return parsedPaymentStatus;
    }

    public void applyPostSuccessPaymentSteps(Payment payment, boolean isEcommPayment) {
        String paymentPrefix;
        if (isEcommPayment) {
            paymentPrefix = "/ecomm/ok [POST].";
        } else {
            paymentPrefix = "Coupon Payment.";
        }

        int appId = payment.getAppId();

        LOGGER.info(paymentPrefix + " Mark application as paid. ApplicationId: {}", appId);
        if (!mainDao.markApplicationAsPaid(appId)) {
            LOGGER.error("MainDao markApplicationAsPaid. Application ID: {}", appId);
        }
        if (isEcommPayment) {
            LOGGER.info(paymentPrefix + " Update payment status in payments db. Payment: {}", payment);
            mainDao.updatePaymentStatus(payment, true, "Ecomm operation success");
        }
        LOGGER.info(paymentPrefix + " Send email [payment receipt] to customer. Payment: {}", payment);
        mailService.sendPlainEmail(payment);
    }

    public void setCouponPaymentActions(ThyApplication app) {
        setCouponUsed(app);
        Payment payment = getPaymentByAppIdAndStatus(app.getId(), 1);
        applyPostSuccessPaymentSteps(payment, false);
    }

}
