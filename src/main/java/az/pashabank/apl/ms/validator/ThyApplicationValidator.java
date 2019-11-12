package az.pashabank.apl.ms.validator;

import az.pashabank.apl.ms.constants.Regex;
import az.pashabank.apl.ms.entity.Branch;
import az.pashabank.apl.ms.entity.CardProduct;
import az.pashabank.apl.ms.entity.ThyApplication;
import az.pashabank.apl.ms.logger.MainLogger;
import az.pashabank.apl.ms.entity.CRSAnswer;
import az.pashabank.apl.ms.model.UploadWrapper;
import az.pashabank.apl.ms.model.thy.CheckTkRequest;
import az.pashabank.apl.ms.model.thy.CheckTkRestResponse;
import az.pashabank.apl.ms.model.thy.EnrollmentChannel;
import az.pashabank.apl.ms.model.thy.MemberData;
import az.pashabank.apl.ms.model.thy.MemberDetails;
import az.pashabank.apl.ms.model.thy.MemberOperations;
import az.pashabank.apl.ms.model.thy.MemberOperationsRequest;
import az.pashabank.apl.ms.model.thy.MemberOperationsResponse;
import az.pashabank.apl.ms.model.thy.MemberProfileData;
import az.pashabank.apl.ms.model.thy.MobilePhone;
import az.pashabank.apl.ms.proxy.ThyServiceProxy;
import az.pashabank.apl.ms.repository.Repositories;
import az.pashabank.apl.ms.utils.ContentTypeUtils;
import az.pashabank.apl.ms.utils.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ThyApplicationValidator {

    private static final MainLogger LOGGER = MainLogger.getLogger(ThyApplicationValidator.class);

    @Value("${upload.folder.thy_applications}")
    protected String uploadFolder;

    @Value("${upload.folder.thy_applications}")
    public void createUploadFolder(String uploadFolder) {
        try {
            Path uploadFolderPath = Paths.get(uploadFolder);
            if (!uploadFolderPath.toFile().exists()) {
                Files.createDirectories(uploadFolderPath);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Value("${ankets.count}")
    protected int anketsCount;

    @Autowired
    private ThyServiceProxy thyServiceProxy;

    @Autowired
    private Repositories repositories;

    public void validateStep1(ThyApplication app, Errors errors) {
        validateName(app, errors);
        validateSurname(app, errors);
        validatePatronymic(app, errors);
        validateBirthDate(app, errors);
        validateResidency(app, errors);
        validateNationality(app, errors);
        validateGender(app, errors);
        validateSecretCode(app, errors);
        validateWorkplace(app, errors);
        validatePosition(app, errors);
    }

    private void validateName(@NotNull ThyApplication app, Errors errors) {
        if (app.getName() == null || app.getName().trim().isEmpty() || !app.getName().matches(Regex.LETTERS)) {
            errors.rejectValue("name","message.errors.invalid.name");
        }
    }

    private void validateSurname(@NotNull ThyApplication app, Errors errors) {
        if (app.getSurname() == null || app.getSurname().trim().isEmpty() || !app.getSurname().matches(Regex.LETTERS)) {
            errors.rejectValue("surname", "message.errors.invalid.surname");
        }
    }

    private void validatePatronymic(@NotNull ThyApplication app, Errors errors) {
        if (app.getPatronymic() == null || app.getPatronymic().trim().isEmpty() || !app.getPatronymic().matches(Regex.LETTERS)) {
            errors.rejectValue("patronymic", "message.errors.invalid.patronymic");
        }
    }

    private void validateBirthDate(@NotNull ThyApplication app, Errors errors) {
        if (app.getBirthDate() == null || app.getBirthDate().trim().isEmpty() || !app.getBirthDate().matches(Regex.DATE)) {
            errors.rejectValue("birthDate", "message.errors.invalid.birth_date");
        }
    }

    private void validateResidency(@NotNull ThyApplication app, Errors errors) {
        if (app.getResidency() == null || app.getResidency().trim().isEmpty()) {
            errors.rejectValue("residency", "message.errors.invalid.residency");
        }
    }

    private void validateNationality(@NotNull ThyApplication app, Errors errors) {
        if (app.getNationality() == null || app.getNationality().trim().isEmpty()) {
            errors.rejectValue("nationality", "message.errors.invalid.nationality");
        }
    }

    private void validateGender(@NotNull ThyApplication app, Errors errors) {
        if (app.getGender() == null || app.getGender().trim().isEmpty()) {
            errors.rejectValue("gender", "message.errors.invalid.gender");
        }
    }

    private void validateSecretCode(@NotNull ThyApplication app, Errors errors) {
        if (app.getSecretCode() == null || app.getSecretCode().trim().length() < 5 || app.getSecretCode().trim().length() > 8 || !app.getSecretCode().matches(Regex.NUMBER)) {
            errors.rejectValue( "secretCode","message.errors.invalid.secret_code");
        }
    }

    private void validateWorkplace(@NotNull ThyApplication app, Errors errors) {
        if (app.getWorkplace() == null || app.getWorkplace().trim().isEmpty()) {
            errors.rejectValue("workplace", "message.errors.invalid.workplace");
        }
    }

    private void validatePosition(@NotNull ThyApplication app, Errors errors) {
        if (app.getPosition() == null || app.getPosition().trim().isEmpty()) {
            errors.rejectValue("position", "message.errors.invalid.position");
        }
    }

    public void validateStep2(ThyApplication app, Errors errors) {
        validateRegistrationCity(app, errors);
        validateRegistrationAddress(app, errors);
        validateMobileNumber(app, errors);
        validateEmail(app, errors);
        if (!app.isDomicileSame()) {
            validateDomicileAddress(app, errors);
        }
    }

    private void validateRegistrationCity(@NotNull ThyApplication app, Errors errors) {
        if (app.getRegistrationCity() == null || app.getRegistrationCity().trim().isEmpty()) {
            errors.rejectValue("registrationCity", "message.errors.invalid.registration_city");
        }
    }

    private void validateRegistrationAddress(@NotNull ThyApplication app, Errors errors) {
        if (app.getRegistrationAddress() == null || app.getRegistrationAddress().trim().isEmpty()) {
            errors.rejectValue("registrationAddress", "message.errors.invalid.registration_address");
        }
    }

    private void validateMobileNumber(@NotNull ThyApplication app, Errors errors) {
        if (app.getMobileNumber() == null || app.getMobileNumber().trim().isEmpty() || !app.getMobileNumber().matches(Regex.PHONE)) {
            errors.rejectValue("mobileNumber", "message.error.invalid.mobile");
        }
    }

    private void validateEmail(@NotNull ThyApplication app, Errors errors) {
        if (app.getEmail() == null || app.getEmail().trim().isEmpty() || !app.getEmail().matches(Regex.EMAIL)) {
            errors.rejectValue("email", "message.error.invalid.email");
        }
    }

    private void validateDomicileAddress(@NotNull ThyApplication app, Errors errors) {
        if (app.getDomicileAddress() == null || app.getDomicileAddress().trim().isEmpty()) {
            errors.rejectValue("domicileAddress", "message.errors.invalid.domicile_address");
        }
    }

    public void validateStep3(ThyApplication app, Errors errors) {
        if (app.isTkNoAvailable()) {
            validateTkNo(app, errors);
        } else {
            validatePassportName(app, errors);
            validatePassportSurname(app, errors);
            validatePassword(app, errors);
            validatePasswordRepeat(app, errors);
            if (!errors.hasErrors()) {
                registerCustomerInThy(app, errors);
            }
        }
    }

    private void validateTkNo(@NotNull ThyApplication app, Errors errors) {
        if (app.getTkNo() == null || app.getTkNo().trim().isEmpty() || !isTkNoVerified(app)) {
            errors.rejectValue("tkNo", "message.errors.invalid.tk_no");
        }
    }

    private boolean isTkNoVerified(ThyApplication app) {
        boolean result = false;
        CheckTkRequest checkTkRequest = new CheckTkRequest(new MemberDetails(app.getTkNo()));
        CheckTkRestResponse checkTkRestResponse = thyServiceProxy.getMemberDetails(checkTkRequest);
        List<MemberData> memberDataKVPair = checkTkRestResponse.getCheckTkReturn().getMemberDataKVPair();
        if (!memberDataKVPair.isEmpty()) {
            String passportName = "";
            String passportSurname = "";
            for (MemberData memberData: memberDataKVPair) {
                switch (memberData.getKey()) {
                    case "D_OUT_TOTAL_MILES":
                    case "D_OUT_FFID": break;
                    case "D_OUT_NAME": passportName = memberData.getValue(); break;
                    case "D_OUT_SURNAME": passportSurname = memberData.getValue(); break;
                    default: LOGGER.info("Key {} is not mapped in object MemberData", memberData.getKey());
                }
            }
            app.setPassportName(passportName);
            app.setPassportSurname(passportSurname);
            result = true;
        }
        return result;
    }

    private void validatePassportName(@NotNull ThyApplication app, Errors errors) {
        if (app.getPassportName() == null || app.getPassportName().trim().isEmpty()) {
            errors.rejectValue("passportName", "message.errors.invalid.passport_name");
        }
    }

    private void validatePassportSurname(@NotNull ThyApplication app, Errors errors) {
        if (app.getPassportSurname() == null || app.getPassportSurname().trim().isEmpty()) {
            errors.rejectValue("passportSurname", "message.errors.invalid.passport_surname");
        }
    }

    private void validatePassword(@NotNull ThyApplication app, Errors errors) {
        if (app.getPassword() == null || app.getPassword().trim().isEmpty()) {
            errors.rejectValue("password", "message.errors.invalid.password");
        }
    }

    private void validatePasswordRepeat(@NotNull ThyApplication app, Errors errors) {
        if (app.getPasswordRepeat() == null || app.getPasswordRepeat().trim().isEmpty() || !arePasswordsSame(app.getPassword(), app.getPasswordRepeat())) {
            errors.rejectValue("passwordRepeat", "message.errors.invalid.password_repeat");
        }
    }

    private boolean arePasswordsSame(String password, String passwordRepeat) {
        return password.equals(passwordRepeat);
    }

    private void registerCustomerInThy(@NotNull ThyApplication app, Errors errors) {
        String birthDate = app.getBirthDate();
        String birthDateDay = birthDate.substring(0, 2);
        String birthDateMonth = birthDate.substring(3, 5);
        String birthDateYear = birthDate.substring(6);

        String mobileNo = app.getMobileNumber();
        String countryCode = mobileNo.substring(1, 4);
        String phoneNumber = mobileNo.substring(4);

        MemberProfileData memberProfileData =
                new MemberProfileData(
                        null, birthDateDay, birthDateMonth, birthDateYear,
                        app.getEmail(), new MobilePhone(countryCode, phoneNumber),
                        app.getPassportName(), app.getPassportSurname(),
                        app.getNationality(), app.getPassword(),
                        "EN", app.getGender().toUpperCase()
                );

        MemberOperations memberOperations = new MemberOperations("CREATE", new EnrollmentChannel(15, 196), memberProfileData);
        MemberOperationsRequest memberOperationsRequest = new MemberOperationsRequest(memberOperations);
        MemberOperationsResponse memberOperationsResponse = thyServiceProxy.createMember(memberOperationsRequest);
        memberProfileData = memberOperationsResponse.getMemberOperationsReturn().getMemberProfileData();
        if (memberProfileData == null) {
            errors.reject("message.errors.register_customer_in_thy");
        } else {
            app.setTkNo(memberProfileData.getMemberId());
        }
    }

    public void validateStep4(ThyApplication app, Errors errors) {
        validateUploads(app, errors);
    }

    private void validateUploads(@NotNull ThyApplication app, Errors errors) {
        if (app.getUploads() == null || app.getUploads().length < 1 || !areUploadedDocsVerified(app.getUploads())) {
            errors.reject("message.errors.invalid.id_card_upload");
        } else {
            List<UploadWrapper> wrappers = fillWrappers(app.getUploads());
            if (!wrappers.isEmpty()) {
                app.setUploadWrappers(wrappers);
            } else {
                errors.reject("message.errors.id_card_upload_files_write");
            }
        }
    }

    private boolean areUploadedDocsVerified(@NotNull MultipartFile[] mfArr) {
        boolean result = true;
        for (MultipartFile mf : mfArr) {
            if (mf.getSize() == 0 || mf.getSize() > 1024 * 1024 * 3 || !mf.getContentType().matches(Regex.UPLOAD_FILE)) {
                result = false;
                break;
            }
        }
        return result;
    }

    private List<UploadWrapper> fillWrappers(@NotNull MultipartFile[] mfs) {
        List<UploadWrapper> wrappers = new ArrayList<>();
        for (MultipartFile mf : mfs) {
            if (!fillWrapper(mf, wrappers)) {
                deleteWrapperFiles(wrappers);
                break;
            }
        }
        return wrappers;
    }

    private boolean fillWrapper(MultipartFile mf, List<UploadWrapper> wrappers) {
        boolean result = true;
        try {
            String fileName = Crypto.getDoubleUuid() + ContentTypeUtils.getExtension(mf.getContentType());
            String location = uploadFolder + "/" + fileName;
            Files.write(Paths.get(location), mf.getBytes());
            UploadWrapper uw = new UploadWrapper();
            uw.setName(fileName);
            uw.setLocation(location);
            uw.setContentType(mf.getContentType());
            uw.setFileSize(mf.getSize());
            wrappers.add(uw);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = false;
        }
        return result;
    }

    public void deleteWrapperFiles(List<UploadWrapper> wrappers) {
        for (UploadWrapper wrapper : wrappers) {
            String wrapperLocation = wrapper.getLocation();
            if (wrapperLocation != null && !wrapperLocation.trim().isEmpty()) {
                try {
                    Files.delete(Paths.get(wrapperLocation));
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        wrappers.clear();
    }

    public void validateStep5(ThyApplication app, Errors errors) {
        validateAnketData(app, errors);
        validateAcceptedTerms(app, errors);
        validateAcceptedGSA(app, errors);
    }

    private void validateAnketData(@NotNull ThyApplication app, Errors errors) {
        for (int i = 0; i < anketsCount; i++) {
            if (!areAnswerAndDescVerified(app.getAnketAnswers()[i], app.getAnketDescs()[i], errors))
                break;
        }
        if (!errors.hasErrors()) {
            fillCrsAnswers(app);
        }
    }

    private boolean areAnswerAndDescVerified(Integer answer, String desc, Errors errors) {
        boolean result = true;
        if (answer == null) {
            errors.reject("ankets.all_questions_required");
            result = false;
        } else if (answer == 1 && desc != null && desc.trim().isEmpty()) {
            errors.reject("ankets.answer_desc.required");
            result = false;
        }
        return result;
    }

    private void fillCrsAnswers(ThyApplication app) {
        List<CRSAnswer> crsAnswers = new ArrayList<>();
        for (int i = 0; i < anketsCount; i++) {
            CRSAnswer crsAnswer = new CRSAnswer();
            crsAnswer.setQuestionId(i + 1);
            crsAnswer.setAnswer(app.getAnketAnswers()[i]);
            crsAnswer.setDescription(app.getAnketDescs()[i]);
            crsAnswers.add(crsAnswer);
        }
        app.setCrsAnswers(crsAnswers);
    }

    private void validateAcceptedTerms(@NotNull ThyApplication app, Errors errors) {
        if (!app.isAcceptedTerms()) {
            errors.reject("message.error.invalid.agreement");
        }
    }

    private void validateAcceptedGSA(@NotNull ThyApplication app, Errors errors) {
        if (!app.isAcceptedGsa()) {
            errors.reject("message.error.invalid.gsa_agreement");
        }
    }

    public void validateStep6(ThyApplication app, Errors errors) {
        validateCardProductId(app, errors);
        validateBranchCode(app, errors);
    }

    private void validateCardProductId(@NotNull ThyApplication app, Errors errors) {
        if (app.getCardProductId() == null || !isCardProductIdVerified(app.getCardProductId())) {
            errors.reject("message.error.invalid.card_type");
        }
    }

    protected boolean isCardProductIdVerified(Integer cardProductId) {
        boolean result = false;
        Optional<CardProduct> opt = repositories.getCardProductRepo().findById(cardProductId);
        if (opt.isPresent()) {
            result = true;
        }
        return result;
    }

    protected void validateBranchCode(@NotNull ThyApplication app, Errors errors) {
        if (app.getBranchCode() == null || !isBranchCodeVerified(app.getBranchCode())) {
            errors.reject("message.error.invalid.branch_id");
        }
    }

    protected boolean isBranchCodeVerified(String branchCode) {
        boolean result = false;
        Optional<Branch> opt = repositories.getBranchRepo().findById(branchCode);
        if (opt.isPresent()) {
            result = true;
        }
        return result;
    }

}
