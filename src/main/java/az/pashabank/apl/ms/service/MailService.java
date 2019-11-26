package az.pashabank.apl.ms.service;


import az.pashabank.apl.ms.dao.MainDao;
import az.pashabank.apl.ms.entity.CRSAnswer;
import az.pashabank.apl.ms.entity.CRSQuestion;
import az.pashabank.apl.ms.entity.CardProduct;
import az.pashabank.apl.ms.entity.CouponCode;
import az.pashabank.apl.ms.entity.ThyApplication;
import az.pashabank.apl.ms.enums.ResultCode;
import az.pashabank.apl.ms.logger.MainLogger;
import az.pashabank.apl.ms.model.OperationResponse;
import az.pashabank.apl.ms.model.Payment;
import az.pashabank.apl.ms.repository.Repositories;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class MailService {
    private static final MainLogger LOGGER = MainLogger.getLogger(MailService.class);

    @Value("${mail.service.username}")
    private String mailUsername;

    @Value("${mail.service.password}")
    private String mailPass;

    @Value("${mail.service.from}")
    private String fromMail;

    @Value("${mail.service.url}")
    private String plainEmailUrl;

    @Value("${mail.service.multipart.url}")
    private String emailUrl;

    @Autowired
    private MainDao mainDao;

    @Autowired
    private MainServiceImpl mainService;

    public OperationResponse sendPlainEmail(Payment payment) {
        OperationResponse operationResponse = new OperationResponse(ResultCode.ERROR);

        try {
            RestTemplate restTemplate = new RestTemplate();
            LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("user", mailUsername);
            map.add("pass", mailPass);
            map.add("html", "1");

            String paymentAmountAzValue = mainDao.getValueFromEmailConfig("payment.amount_az");
            String paymentCardAzValue = mainDao.getValueFromEmailConfig("payment.card_az");
            String paymentAmountEnValue = mainDao.getValueFromEmailConfig("payment.amount_en");
            String paymentCardEnValue = mainDao.getValueFromEmailConfig("payment.card_en");
            String paymentEnd1Value = mainDao.getValueFromEmailConfig("payment.end1");
            String paymentEnd2Value = mainDao.getValueFromEmailConfig("payment.end2");

            int appId = payment.getAppId();

            ThyApplication app = mainService.getApplicationById(appId);

            String paymentMethod = null;
            boolean isUrgent = false;

            if (app != null) {
                paymentMethod = app.getPaymentMethod();
                isUrgent = app.isUrgent();
            }

            String cardPaymentBeginValue = mainDao.getValueFromEmailConfig("payment.card.begin");

            StringBuilder plainTemplateBuilder = new StringBuilder(cardPaymentBeginValue);
            buildMiddlePlainTemplate(
                    paymentMethod, isUrgent, plainTemplateBuilder, "az",
                    paymentAmountAzValue, paymentCardAzValue, paymentAmountEnValue, paymentCardEnValue
            );
            plainTemplateBuilder.append(paymentEnd1Value);
            plainTemplateBuilder.append(paymentEnd2Value);

            String couponSerialNo = null;
            if (paymentMethod != null && paymentMethod.equalsIgnoreCase("COUPON")) {
//                couponSerialNo = mainDao.getCouponSerialNo(appId);
                CouponCode couponCode = mainService.getCouponCodeByAppId(appId);
                if (couponCode != null) {
                    couponSerialNo = couponCode.getSerialNo();
                }
            }

            String date = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(payment.getCreateDate());
            String message;

            if (paymentMethod != null && paymentMethod.equalsIgnoreCase("CARD")) {
                message = String.format(
                        plainTemplateBuilder.toString(),
                        /*for Azerbaijan language*/
                        payment.getTransactionId(),
                        "Kart",
                        payment.getAmount(),
                        payment.getCurrency(),
                        payment.getEcommCardNumber(),
                        date,
                        /*for English language*/
                        payment.getTransactionId(),
                        "Card",
                        payment.getAmount(),
                        payment.getCurrency(),
                        payment.getEcommCardNumber(),
                        date
                );
            } else if (isUrgent) {
                message = String.format(
                        plainTemplateBuilder.toString(),
                        /*for Azerbaijan language*/
                        payment.getTransactionId(),
                        "Kupon",
                        couponSerialNo != null ? couponSerialNo : "",
                        payment.getAmount(),
                        payment.getCurrency(),
                        payment.getEcommCardNumber(),
                        date,
                        /*for English language*/
                        payment.getTransactionId(),
                        "Coupon",
                        couponSerialNo != null ? couponSerialNo : "",
                        payment.getAmount(),
                        payment.getCurrency(),
                        payment.getEcommCardNumber(),
                        date
                );
            } else {
                message = String.format(
                        plainTemplateBuilder.toString(),
                        /*for Azerbaijan language*/
                        payment.getTransactionId(),
                        "Kupon",
                        couponSerialNo != null ? couponSerialNo : "",
                        payment.getAmount(),
                        payment.getCurrency(),
                        date,
                        /*for English language*/
                        payment.getTransactionId(),
                        "Coupon",
                        couponSerialNo != null ? couponSerialNo : "",
                        payment.getAmount(),
                        payment.getCurrency(),
                        date
                );
            }

            String subject = "PASHA THY Miles & Smiles Hesabaz Card Order payment receipt";
            String toMail = app.getEmail();

            map.add("message", URLEncoder.encode(message, "UTF-8"));
            map.add("subject", subject);
            map.add("to", toMail);
            map.add("from", fromMail);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            String result = restTemplate.postForObject(plainEmailUrl, request, String.class);

            JSONObject jsono = new JSONObject(result);
            operationResponse.setCode(jsono.getEnum(ResultCode.class, "code"));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return operationResponse;
    }

    private void buildMiddlePlainTemplate(
            String paymentMethod, boolean isUrgent, StringBuilder plainTemplateBuilder, String lang,
            String paymentAmountAzValue, String paymentCardAzValue, String paymentAmountEnValue, String paymentCardEnValue
    ) {
        String cardPaymentCouponSerialAzValue = mainDao.getValueFromEmailConfig("payment.card.coupon_serial_az");
        String cardPaymentMiddleValue = mainDao.getValueFromEmailConfig("payment.card.middle");
        String cardPaymentCouponSerialEnValue = mainDao.getValueFromEmailConfig("payment.card.coupon_serial_en");
        if (lang.equals("az")) {
            if (paymentMethod != null && paymentMethod.equalsIgnoreCase("COUPON")) {
                plainTemplateBuilder.append(cardPaymentCouponSerialAzValue);
            }
            plainTemplateBuilder.append(paymentAmountAzValue);
            if (isUrgent || (paymentMethod != null && paymentMethod.equalsIgnoreCase("CARD"))) {
                plainTemplateBuilder.append(paymentCardAzValue);
            }
            plainTemplateBuilder.append(cardPaymentMiddleValue);
            buildMiddlePlainTemplate(
                    paymentMethod, isUrgent, plainTemplateBuilder, "en",
                    paymentAmountAzValue, paymentCardAzValue, paymentAmountEnValue, paymentCardEnValue
            );
        } else if (lang.equals("en")) {
            if (paymentMethod != null && paymentMethod.equalsIgnoreCase("COUPON")) {
                plainTemplateBuilder.append(cardPaymentCouponSerialEnValue);
            }
            plainTemplateBuilder.append(paymentAmountEnValue);
            if (isUrgent || (paymentMethod != null && paymentMethod.equalsIgnoreCase("CARD"))) {
                plainTemplateBuilder.append(paymentCardEnValue);
            }
        }
    }

    public OperationResponse sendEmail(ThyApplication application, List<CRSQuestion> crsQuestions, Payment payment, List<String> toEmails, List<Resource> uploads) {
        OperationResponse operationResponse = new OperationResponse(ResultCode.ERROR);

        try {
            LOGGER.info("Send email. ThyApplication: " + application + ", Emails: " + toEmails + ", Uploads: " + uploads);
            RestTemplate restTemplate = new RestTemplate();

            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("user", mailUsername);
            map.add("pass", mailPass);
            for (Resource r : uploads) {
                map.add("attachments", r);
            }
            map.add("message", URLEncoder.encode(generateMailContent(application, crsQuestions, payment), "UTF-8"));
            String subject = String.format("Miles&Smiles Hesabaz card order %s %s %s", application.getName(), application.getSurname(), application.getPatronymic());
            map.add("subject", subject);
            for (String s : toEmails) {
                map.add("to", s);
            }
            map.add("from", fromMail);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.add("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(emailUrl + "?user=pasha_life_app&pass=pA5haL!f3", HttpMethod.POST, requestEntity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String result = responseEntity.getBody();
                JSONObject jsono = new JSONObject(result);
                operationResponse.setCode(jsono.getEnum(ResultCode.class, "code"));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return operationResponse;
    }

    private @NotNull String generateMailContent(@NotNull ThyApplication application, List<CRSQuestion> crsQuestions, Payment payment) {
        String thyOtrsRequestValue = mainDao.getValueFromEmailConfig("thy_hesabaz_otrs_request");
        CardProduct cardProduct = mainService.getCardProductById(application.getCardProductId());
        String plainTemplate = String.format(
                thyOtrsRequestValue,
                application.getResidency(),
                application.getNationality(),
                application.getName(),
                application.getSurname(),
                application.getPatronymic(),
                application.getGender(),
                application.getBirthDate(),
                application.getRegistrationCity(),
                application.getRegistrationAddress(),
                application.getDomicileAddress(),
                application.getMobileNumber(),
                application.getEmail(),
                application.getSecretCode(),
                application.getWorkplace(),
                application.getPosition(),
                application.isUrgent(),
                application.getTkNo(),
                application.getPassportName(),
                application.getPassportSurname(),
                application.getCurrency(),
                cardProduct != null ? cardProduct.getName() : "",
                application.getPeriod(),
                application.getBranchName(),
                application.getPaymentMethod(),
                String.valueOf(application.getAmountToPay()),
                "AZN",
                payment.getPaymentDesc() != null ? payment.getPaymentDesc() : "",
                application.getRoamingNumber() != null ? application.getRoamingNumber() : ""
        );

        StringBuilder output = new StringBuilder(plainTemplate);

        // Generating self certification form questions and the user's answers to them
        try {
            List<CRSAnswer> answers = application.getCrsAnswers();
            for (int i = 1; i <= crsQuestions.size(); i++) {
                CRSAnswer answer = answers.get(i - 1);
                String row = String.format(
                        "%d. %s%n%s. %s%n",
                        i,
                        crsQuestions.get(i - 1).getQuestion(),
                        answer.getAnswer() == 1 ? "Yes" : "No",
                        answer.getDescription() == null ? "" : answer.getDescription().trim()
                );
                output.append(row);
            }
            String row = String.format("%d. %s%n%s. %n",12,"OWNER_INFORMATION","Yes");
            output.append(row);
            row = String.format("%d. %s%n%s. %n",13,"REAL_UBO","Yes");
            output.append(row);
            row = String.format("%d. %s%n%s. %n",14,"REASON_FOR_OPENING_ACCOUNT","MAIN BANKNG RELATIONSHIP");
            output.append(row);
            row = String.format("%d. %s%n%s. %n",15,"VAT_TAXPAYER","No");
            output.append(row);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return output.toString();
    }

}
