package az.pashabank.apl.ms.controller;

import az.pashabank.apl.ms.entity.Branch;
import az.pashabank.apl.ms.entity.CRSAnswer;
import az.pashabank.apl.ms.entity.CRSQuestion;
import az.pashabank.apl.ms.entity.CardProduct;
import az.pashabank.apl.ms.entity.City;
import az.pashabank.apl.ms.entity.Country;
import az.pashabank.apl.ms.entity.ThyApplication;
import az.pashabank.apl.ms.entity.UploadWrapper;
import az.pashabank.apl.ms.logger.MainLogger;
import az.pashabank.apl.ms.service.MainServiceImpl;
import az.pashabank.apl.ms.validator.ThyApplicationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

@Controller
public class WebController {

    private static final MainLogger LOGGER = MainLogger.getLogger(WebController.class);

    @Autowired
    private MainServiceImpl mainService;

    @Autowired
    ThyApplicationValidator validator;

    @GetMapping(value = {"", "/", "/index", "/step1"})
    public String showStep1(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        if (httpSession.getAttribute("sessionApp") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
            resetApp(app, sessionApp);
        }
        reloadStep1(model, locale);
        return "index";
    }

    private void resetApp(ThyApplication app, ThyApplication app2) {
        app.setName(app2.getName());
        app.setSurname(app2.getSurname());
        app.setPatronymic(app2.getPatronymic());
        app.setBirthDate(app2.getBirthDate());
        app.setResidency(app2.getResidency());
        app.setNationality(app2.getNationality());
        app.setGender(app2.getGender());
        app.setSecretCode(app2.getSecretCode());
        app.setWorkplace(app2.getWorkplace());
        app.setPosition(app2.getPosition());
    }

    private void reloadStep1(Model model, Locale locale) {
        model.addAttribute("lcl", locale.getLanguage());
        List<Country> countryList = mainService.getCountryList(locale.getLanguage());
        model.addAttribute("countryList", countryList);
    }

    @PostMapping("/index")
    public String postStep1(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        validator.validateStep1(app, result);
        if (result.hasErrors()) {
            reloadStep1(model, locale);
            return "index";
        }
        if (httpSession.getAttribute("sessionApp") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
            resetApp(sessionApp, app);
            sessionApp.setStep(1);
            mainService.saveApplication(sessionApp);
            httpSession.setAttribute("sessionApp", sessionApp);
        } else {
            app.setStep(1);
            mainService.saveApplication(app);
            app.setDomicileSame(true);
            app.setTkNoAvailable(true);
            httpSession.setAttribute("sessionApp", app);
        }
        return "redirect:/step2";
    }

    @GetMapping("/step2")
    public String showStep2(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            reloadStep1(model, locale);
            return "index";
        }
        resetStep2FromSession(app, sessionApp);
        reloadStep2(model, locale, sessionApp);
        return "step2";
    }

    private void resetStep2FromSession(ThyApplication app, ThyApplication sessionApp) {
        app.setRegistrationCity(sessionApp.getRegistrationCity());
        app.setRegistrationAddress(sessionApp.getRegistrationAddress());
        app.setMobileNumber(sessionApp.getMobileNumber());
        app.setEmail(sessionApp.getEmail());
        app.setDomicileSame(sessionApp.isDomicileSame());
        app.setDomicileAddress(sessionApp.getDomicileAddress());
    }

    private void reloadStep2(Model model, Locale locale, ThyApplication app) {
        model.addAttribute("lcl", locale.getLanguage());
        List<City> cityList = mainService.getCityList(app.getNationality());
        model.addAttribute("cityList", cityList);
        model.addAttribute("isDomicileSame", app.isDomicileSame());
    }

    @PostMapping(value = "/step2", params = {"next"})
    public String postStep2(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            reloadStep1(model, locale);
            return "index";
        }
        validator.validateStep2(app, result);
        if (result.hasErrors()) {
            app.setNationality(sessionApp.getNationality());
            reloadStep2(model, locale, app);
            return "step2";
        }
        setStep2SessionApp(app, sessionApp);
        sessionApp.setStep(2);
        mainService.saveApplication(sessionApp);
        httpSession.setAttribute("sessionApp", sessionApp);
        return "redirect:/step3";
    }

    private void setStep2SessionApp(ThyApplication app, ThyApplication sessionApp) {
        sessionApp.setRegistrationCity(app.getRegistrationCity());
        sessionApp.setRegistrationAddress(app.getRegistrationAddress());
        sessionApp.setMobileNumber(app.getMobileNumber());
        sessionApp.setEmail(app.getEmail());
        sessionApp.setDomicileSame(app.isDomicileSame());
        sessionApp.setDomicileAddress(app.getDomicileAddress());
    }

    @PostMapping(value = "/step2", params = {"back"})
    public String postStep2Back(@ModelAttribute("app") ThyApplication app, HttpSession httpSession) {
        if (httpSession.getAttribute("sessionApp") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
            setStep2SessionApp(app, sessionApp);
            httpSession.setAttribute("sessionApp", sessionApp);
        }
        return "redirect:/index";
    }

    @GetMapping("/step3")
    public String showStep3(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            reloadStep1(model, locale);
            return "index";
        }
        resetStep3FromSession(app, sessionApp);
        model.addAttribute("lcl", locale.getLanguage());
        model.addAttribute("isTkNoAvailable", app.isTkNoAvailable());
        app.setTkNoAvailable(true);
        model.addAttribute("isTkNoAvailable", true);
        return "step3";
    }

    private void resetStep3FromSession(ThyApplication app, ThyApplication sessionApp) {
        app.setTkNoAvailable(sessionApp.isTkNoAvailable());
        app.setTkNo(sessionApp.getTkNo());
        app.setPassportName(sessionApp.getPassportName());
        app.setPassportSurname(sessionApp.getPassportSurname());
    }

    @PostMapping(value = "/step3", params = {"next"})
    public String postStep3(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            reloadStep1(model, locale);
            return "index";
        }
        /*if (!app.isTkNoAvailable()) {
            app.setBirthDate(sessionApp.getBirthDate());
            app.setMobileNumber(sessionApp.getMobileNumber());
            app.setEmail(sessionApp.getEmail());
            app.setNationality(sessionApp.getNationality());
            app.setGender(sessionApp.getGender());
        }
        validator.validateStep3(app, result);
        if (result.hasErrors()) {
            model.addAttribute("lcl", locale.getLanguage());
            model.addAttribute("isTkNoAvailable", app.isTkNoAvailable());
            return "step3";
        }*/
        setStep3SessionApp(app, sessionApp);
        sessionApp.setStep(3);
        mainService.saveApplication(sessionApp);
        httpSession.setAttribute("sessionApp", sessionApp);
        return "redirect:/step4";
    }

    private void setStep3SessionApp(ThyApplication app, ThyApplication sessionApp) {
        sessionApp.setTkNoAvailable(app.isTkNoAvailable());
        sessionApp.setTkNo(app.getTkNo());
        sessionApp.setPassportName(app.getPassportName());
        sessionApp.setPassportSurname(app.getPassportSurname());
    }

    @PostMapping(value = "/step3", params = {"back"})
    public String postStep3Back(@ModelAttribute("app") ThyApplication app, HttpSession httpSession) {
        if (httpSession.getAttribute("sessionApp") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
            setStep3SessionApp(app, sessionApp);
            httpSession.setAttribute("sessionApp", sessionApp);
        }
        return "redirect:/step2";
    }

    @GetMapping("/step4")
    public String showStep4(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            reloadStep1(model, locale);
            return "index";
        }
        app.setUploads(sessionApp.getUploads());
        model.addAttribute("lcl", locale.getLanguage());
        return "step4";
    }

    @PostMapping(value = "/step4", params = {"next"})
    public String postStep4(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            reloadStep1(model, locale);
            return "index";
        }
        validator.validateStep4(app, result);
        if (result.hasErrors()) {
            model.addAttribute("lcl", locale.getLanguage());
            return "step4";
        }
        for (UploadWrapper uploadWrapper : app.getUploadWrappers()) {
            uploadWrapper.setApp(sessionApp);
        }
        sessionApp.setUploadWrappers(app.getUploadWrappers());
        sessionApp.setUploads(app.getUploads());
        sessionApp.setStep(4);
        mainService.saveApplication(sessionApp);
        httpSession.setAttribute("sessionApp", sessionApp);
        return "redirect:/step5";
    }

    @PostMapping(value = "/step4", params = {"back"})
    public String postStep4Back(@ModelAttribute("app") ThyApplication app, HttpSession httpSession) {
        if (httpSession.getAttribute("sessionApp") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
            sessionApp.setUploads(app.getUploads());
            httpSession.setAttribute("sessionApp", sessionApp);
        }
        return "redirect:/step3";
    }

    @GetMapping("/step5")
    public String showStep5(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            reloadStep1(model, locale);
            return "index";
        }
        resetStep5FromSession(app, sessionApp);
        reloadStep5(model, locale, app);
        return "step5";
    }

    private void resetStep5FromSession(ThyApplication app, ThyApplication sessionApp) {
        app.setAnketAnswers(sessionApp.getAnketAnswers());
        app.setAnketDescs(sessionApp.getAnketDescs());
        app.setAcceptedTerms(sessionApp.isAcceptedTerms());
        app.setAcceptedGsa(sessionApp.isAcceptedGsa());
    }

    private void reloadStep5(Model model, Locale locale, ThyApplication app) {
        model.addAttribute("lcl", locale.getLanguage());
        List<CRSQuestion> crsQuestionsList = mainService.getCRSQuestionList(locale.getLanguage());
        model.addAttribute("crsQuestionList", crsQuestionsList);
        model.addAttribute("anketAnswers", app.getAnketAnswers());
    }

    @PostMapping(value = "/step5", params = {"next"})
    public String postStep5(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            reloadStep1(model, locale);
            return "index";
        }
        validator.validateStep5(app, result, locale);
        if (result.hasErrors()) {
            reloadStep5(model, locale, app);
            return "step5";
        }
        for (CRSAnswer crsAnswer : app.getCrsAnswers()) {
            crsAnswer.setApp(sessionApp);
        }
        sessionApp.setCrsAnswers(app.getCrsAnswers());
        setStep5SessionApp(app, sessionApp);
        sessionApp.setStep(5);
        mainService.saveApplication(sessionApp);
        httpSession.setAttribute("sessionApp", sessionApp);
        return "redirect:/step6";
    }

    private void setStep5SessionApp(ThyApplication app, ThyApplication sessionApp) {
        sessionApp.setAnketAnswers(app.getAnketAnswers());
        sessionApp.setAnketDescs(app.getAnketDescs());
        sessionApp.setAcceptedTerms(app.isAcceptedTerms());
        sessionApp.setAcceptedGsa(app.isAcceptedGsa());
    }

    @PostMapping(value = "/step5", params = {"back"})
    public String postStep5Back(@ModelAttribute("app") ThyApplication app, HttpSession httpSession) {
        if (httpSession.getAttribute("sessionApp") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
            setStep5SessionApp(app, sessionApp);
            httpSession.setAttribute("sessionApp", sessionApp);
        }
        return "redirect:/step4";
    }

    @GetMapping("/step6")
    public String showStep6(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            reloadStep1(model, locale);
            return "index";
        }
        resetStep6FromSession(app, sessionApp);
        reloadStep6(model, locale, app);
        return "step6";
    }

    private void resetStep6FromSession(ThyApplication app, ThyApplication sessionApp) {
        app.setCardProductId(sessionApp.getCardProductId());
        app.setBranchCode(sessionApp.getBranchCode());
        app.setUrgent(sessionApp.isUrgent());
    }

    private void reloadStep6(Model model, Locale locale, ThyApplication app) {
        model.addAttribute("lcl", locale.getLanguage());
        /*List<CardProduct> cardProductList = Arrays.asList(
                new CardProduct(4590, "BLACK", 10, 250),
                new CardProduct(4684, "ELITE", 10, 600)
        );*/
        List<CardProduct> cardProductList = mainService.getCardProductList();
        model.addAttribute("cardProductList", cardProductList);
        List<Branch> branchList = mainService.getBranchList(locale.getLanguage());
        model.addAttribute("branchList", branchList);
        boolean emptyBranch = app.getBranchCode() != null ? app.getBranchCode().trim().isEmpty() : true;
        model.addAttribute("emptyBranch", emptyBranch);
    }

    @PostMapping(value = "/step6", params = {"next"})
    public String postStep6(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            reloadStep1(model, locale);
            return "index";
        }
        validator.validateStep6(app, result, locale);
        if (result.hasErrors()) {
            reloadStep6(model, locale, app);
            return "step6";
        }
        sessionApp.setCardProductId(app.getCardProductId());
        sessionApp.setPeriod(3);
        sessionApp.setBranchCode(app.getBranchCode());
        sessionApp.setBranchName(app.getBranchName());
        sessionApp.setCurrency("AZN");
        sessionApp.setUrgent(app.isUrgent());
        sessionApp.setAmountToPay(app.getAmountToPay());
        sessionApp.setStep(6);
        mainService.saveApplication(sessionApp);
        httpSession.setAttribute("sessionApp", sessionApp);
        return "redirect:/step7";
    }

    @PostMapping(value = "/step6", params = {"back"})
    public String postStep6Back(@ModelAttribute("app") ThyApplication app, HttpSession httpSession) {
        if (httpSession.getAttribute("sessionApp") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
            sessionApp.setCardProductId(app.getCardProductId());
            sessionApp.setBranchCode(app.getBranchCode());
            sessionApp.setUrgent(app.isUrgent());
            httpSession.setAttribute("sessionApp", sessionApp);
        }
        return "redirect:/step5";
    }

    @GetMapping("/step7")
    public String showStep7(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            reloadStep1(model, locale);
            return "index";
        }
//        sessionApp.setAmountToPay(1);
        reloadStep7(model, locale, sessionApp);
        return "step7";
    }

    private void reloadStep7(Model model, Locale locale, ThyApplication app) {
        Country country = mainService.getCountryByLangAndCode(locale.getLanguage(), app.getNationality());
        City city = mainService.getCityByCountryCodeAndCode(app.getNationality(), app.getRegistrationCity());
        CardProduct cardProduct = mainService.getCardProductById(app.getCardProductId());
        model.addAttribute("nationality", country != null ? country.getName() : "");
        model.addAttribute("registrationCity", city != null ? city.getName() : "");
        model.addAttribute("cardProductName", cardProduct != null ? cardProduct.getName() : "");
        model.addAttribute("sessionApp", app);
        model.addAttribute("lcl", locale.getLanguage());
    }

    @PostMapping(value = "/step7", params = {"pay"})
    public String postStep7(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            reloadStep1(model, locale);
            return "index";
        }
        sessionApp.setPaymentMethod("CARD");
        sessionApp.setActive(true);
        sessionApp.setStep(7);
        mainService.saveApplication(sessionApp);
        httpSession.setAttribute("sessionApp", sessionApp);

        String redirectView = mainService.registerPaymentAndGetView(sessionApp, locale, result);
        if (result.hasErrors()) {
            reloadStep7(model, locale, sessionApp);
            return "step7";
        }
        return redirectView;
    }

    @PostMapping(value = "/step7", params = {"cancel"})
    public String postStep7Back() {
        return "redirect:/index";
    }

    @GetMapping("/step8")
    public String showStep8(Model model, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp == null) {
            LOGGER.error("step8 ERROR. Session app is NULL");
            model.addAttribute("lcl", locale.getLanguage());
            return "error";
        }
        mainService.setCouponPaymentActions(sessionApp);
        model.addAttribute("lcl", locale.getLanguage());
        return "step8";
    }

    @PostMapping(value = "/step8")
    public String postStep8(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/index";
    }

    @PostMapping("/ecomm/fail")
    protected String showEcommFail(
            Model model,
            @RequestParam(value = "trans_id", defaultValue = "") String ecommTransId,
            HttpSession httpSession,
            Locale locale
    ) {
        LOGGER.info("/ecomm/fail [POST]. ecommTransId: {}", ecommTransId);
        model.addAttribute("lcl", locale.getLanguage());
        httpSession.invalidate();
        return "error";
    }

    @PostMapping("/ecomm/ok")
    protected String showEcommOK(
            Model model,
            @RequestParam(value = "trans_id", defaultValue = "") String ecommTransId,
            HttpSession httpSession,
            Locale locale
    ) {
        model.addAttribute("lcl", locale.getLanguage());
        String redirectView = mainService.checkPaymentStatusAndGetView(ecommTransId, locale, model);
        httpSession.invalidate();
        return redirectView;
    }

}
