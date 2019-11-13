package az.pashabank.apl.ms.controller;

import az.pashabank.apl.ms.entity.CRSQuestion;
import az.pashabank.apl.ms.entity.City;
import az.pashabank.apl.ms.entity.Country;
import az.pashabank.apl.ms.entity.ThyApplication;
import az.pashabank.apl.ms.logger.MainLogger;
import az.pashabank.apl.ms.model.thy.RegisterCustomerInThyRequest;
import az.pashabank.apl.ms.service.MainServiceImpl;
import az.pashabank.apl.ms.validator.ThyApplicationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
            app = (ThyApplication) httpSession.getAttribute("sessionApp");
        }
        reloadStep1(model, locale);
        return "index";
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
        app.setStep(1);
        mainService.saveApplication(app);
        httpSession.setAttribute("sessionApp", app);
        return "redirect:/step2";
    }

    @GetMapping("/step2")
    public String showStep2(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        if (httpSession.getAttribute("sessionApp") == null) {
            reloadStep1(model, locale);
            return "index";
        }
        app = (ThyApplication) httpSession.getAttribute("sessionApp");
        reloadStep2(model, locale, app);
        return "step2";
    }

    private void reloadStep2(Model model, Locale locale, ThyApplication app) {
        model.addAttribute("lcl", locale.getLanguage());
        List<City> cityList = mainService.getCityList(app.getNationality());
        model.addAttribute("cityList", cityList);
        app.setDomicileSame(app.getStep() < 2 ? true : app.isDomicileSame());
        model.addAttribute("isDomicileSame", app.isDomicileSame());
    }

    @PostMapping(value = "/step2", params = {"next"})
    public String postStep2(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        if (httpSession.getAttribute("sessionApp") == null) {
            reloadStep1(model, locale);
            return "index";
        }
        validator.validateStep2(app, result);
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (result.hasErrors()) {
            reloadStep2(model, locale, sessionApp);
            return "step2";
        }
        setStep2SessionAppInfo(app, sessionApp);
        sessionApp.setStep(2);
        mainService.saveApplication(sessionApp);
        httpSession.setAttribute("sessionApp", sessionApp);
        model.addAttribute("lcl", locale.getLanguage());
        return "redirect:/step3";
    }

    private void setStep2SessionAppInfo(ThyApplication app, ThyApplication sessionApp) {
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
            setStep2SessionAppInfo(app, sessionApp);
            httpSession.setAttribute("sessionApp", sessionApp);
        }
        return "redirect:/index";
    }

    @GetMapping("/step3")
    public String showStep3(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        if (httpSession.getAttribute("sessionApp") == null) {
            reloadStep1(model, locale);
            return "index";
        }
        app = (ThyApplication) httpSession.getAttribute("sessionApp");
        model.addAttribute("lcl", locale.getLanguage());
        app.setTkNoAvailable(app.getStep() < 3 ? true : app.isTkNoAvailable());
        model.addAttribute("isTkNoAvailable", app.isTkNoAvailable());
        return "step3";
    }

    @PostMapping(value = "/step3", params = {"next"})
    public String postStep3(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        if (httpSession.getAttribute("sessionApp") == null) {
            reloadStep1(model, locale);
            return "index";
        }
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (!app.isTkNoAvailable() && sessionApp != null) {
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
        }
        setStep3SessionAppInfo(app, sessionApp);
        sessionApp.setStep(3);
        mainService.saveApplication(sessionApp);
        httpSession.setAttribute("sessionApp", sessionApp);
        return "redirect:/step4";
    }

    private void setStep3SessionAppInfo(ThyApplication app, ThyApplication sessionApp) {
        sessionApp.setTkNoAvailable(app.isTkNoAvailable());
        sessionApp.setTkNo(app.getTkNo());
        sessionApp.setPassportName(app.getPassportName());
        sessionApp.setPassportSurname(app.getPassportSurname());
    }

    @PostMapping(value = "/step3", params = {"back"})
    public String postStep3Back(@ModelAttribute("app") ThyApplication app, HttpSession httpSession) {
        if (httpSession.getAttribute("sessionApp") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
            setStep3SessionAppInfo(app, sessionApp);
            httpSession.setAttribute("sessionApp", sessionApp);
        }
        return "redirect:/step2";
    }

    @GetMapping("/step4")
    public String showStep4(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        if (httpSession.getAttribute("sessionApp") == null) {
            reloadStep1(model, locale);
            return "index";
        }
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
        sessionApp.setUploadWrappers(app.getUploadWrappers());
        sessionApp.setStep(4);
        mainService.saveApplication(sessionApp);
        httpSession.setAttribute("sessionApp", sessionApp);
        return "redirect:/step5";
    }

    @PostMapping(value = "/step4", params = {"back"})
    public String postStep4Back(HttpSession httpSession) {
        return "redirect:/step3";
    }

    @GetMapping("/step5")
    public String showStep5(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        /*if (httpSession.getAttribute("sessionApp") == null) {
            reloadStep1(model, locale);
            return "index";
        }*/
        reloadStep5(model, locale);
        return "step5";
    }

    private void reloadStep5(Model model, Locale locale) {
        model.addAttribute("lcl", locale.getLanguage());
        List<CRSQuestion> crsQuestionsList = mainService.getCRSQuestionList(locale.getLanguage());
        model.addAttribute("crsQuestionList", crsQuestionsList);
    }

    @PostMapping(value = "/step5", params = {"next"})
    public String postStep5(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        validator.validateStep5(app, result);
        if (result.hasErrors()) {
            reloadStep5(model, locale);
            return "step5";
        }
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp != null) {
            sessionApp.setCrsAnswers(app.getCrsAnswers());
            sessionApp.setAcceptedTerms(app.isAcceptedTerms());
            sessionApp.setAcceptedGsa(app.isAcceptedGsa());
            sessionApp = mainService.saveApplication(sessionApp);
            httpSession.setAttribute("sessionApp", sessionApp);
            return "redirect:/step6";
        } else {
            return "redirect:/index";
        }
    }

    @PostMapping(value = "/step5", params = {"back"})
    public String postStep5Back(HttpSession httpSession) {
        if (httpSession.getAttribute("sessionApp") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
            httpSession.removeAttribute("sessionApp");
        }
        return "redirect:/index";
    }

    @GetMapping("/step6")
    public String showStep6(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        model.addAttribute("lcl", locale.getLanguage());
        if (httpSession.getAttribute("sessionApp") == null) {
            return "index";
        } else {
            return "step6";
        }
    }

    @PostMapping(value = "/step6", params = {"next"})
    public String postStep6(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        validator.validateStep6(app, result);
        if (result.hasErrors()) {
            model.addAttribute("lcl", locale.getLanguage());
            return "step6";
        }
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
        if (sessionApp != null) {
            sessionApp.setCardProductId(app.getCardProductId());
            sessionApp.setBranchCode(app.getBranchCode());
            sessionApp = mainService.saveApplication(sessionApp);
            httpSession.removeAttribute("sessionApp");
            model.addAttribute("sessionApp", sessionApp);
            return "submit";
        } else {
            reloadStep1(model, locale);
            return "redirect:/index";
        }
    }

    @PostMapping(value = "/step6", params = {"back"})
    public String postStep6Back(HttpSession httpSession) {
        if (httpSession.getAttribute("sessionApp") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("sessionApp");
            httpSession.removeAttribute("sessionApp");
        }
        return "redirect:/index";
    }

    /*@PostMapping(value = {"/general-info"})
    public String postGeneralInfo(Model model, HttpSession httpSession, Locale locale){
        mainService.newApplicationStep1(getMockData());
        return "index";
    }*/

    @PostMapping("registerCustomerInThy")
    public String registerCustomerInThy(
            @PathVariable final String lang,
            @RequestBody final RegisterCustomerInThyRequest request) {
        LOGGER.info("registerCustomerInThy. request: {}, lang {}", request, lang);
        // OperationResponse<RegisterCustomerInThyResponse> response = cardService.registerCustomerInThy(request);
        // LOGGER.info("registerCustomerInThy. response: {}", response);
        return "index";
    }

    private ThyApplication getMockData() {
        ThyApplication thyApplication = new ThyApplication();
//        thyApplication.setName("Mahmud");
//        thyApplication.setSurname("Aliyev");
        return thyApplication;
    }

    /*@GetMapping(value = {URL.EMPTY, URL.SLASH, URL.NEW_ORDER})
    protected String showOrderPage(Model model, Locale locale) {
        try {
            NewOrder newOrder = new NewOrder();
            newOrder.setDomicileIsSame(Boolean.TRUE);

            model.addAttribute("form", form);
            model.addAttribute(CARDS, pashaCardOrderService.getActiveCards("AZN", 1, "P"));
            model.addAttribute(BRANCHES, pashaCardOrderService.getBranches(locale));
            model.addAttribute(ANKETS_COUNT, anketsCount);
            model.addAttribute(MAX_CARD_COUNT, maksimumCardCount);
            model.addAttribute("lang", locale.getLanguage());
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return "debit";
    }

    @PostMapping(URL.NEW_CARD_ORDER)
    public OperationResponse createNewCustomerOrder(
            @RequestBody final CreateNewCustomerOrderRequest request,
            @PathVariable final String lang
    ) {
        LOGGER.info("createNewCustomerOrder. request: {}, lang: {}", request, lang);
        OperationResponse<CreateNewCustomerOrderResponse> response = cardService.createNewCustomerOrder(request, lang);
        LOGGER.info("createNewCustomerOrder. response: {}", response);
        return response;
    }*/
}
