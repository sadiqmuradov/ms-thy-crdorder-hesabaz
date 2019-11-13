package az.pashabank.apl.ms.controller;

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

    private Model reloadStep1(Model model, Locale locale) {
        model.addAttribute("lcl", locale.getLanguage());
        List<Country> countryList = mainService.getCountryList(locale.getLanguage());
        model.addAttribute("countryList", countryList);
        return model;
    }

    private Model reloadStep2(Model model, Locale locale, ThyApplication app) {
        model.addAttribute("lcl", locale.getLanguage());
        List<City> cityList = mainService.getCityList(app.getNationality());
        model.addAttribute("cityList", cityList);
        app.setDomicileSame(true);
        model.addAttribute("isDomicileSame", app.isDomicileSame());
        return model;
    }

    @GetMapping(value = {"", "/", "/index"})
    public String showIndex(Model model, @ModelAttribute("app") ThyApplication app, Locale locale) {
        model = reloadStep1(model, locale);
        return "index";
    }

    @PostMapping("/index")
    public String postIndex(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        validator.validateStep1(app, result);
        if (result.hasErrors()) {
            model = reloadStep1(model, locale);
            return "index";
        }
        app = mainService.saveApplication(app);
        httpSession.setAttribute("app", app);
        return "redirect:/step2";
    }

    @GetMapping("/step2")
    public String showStep2(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        if (httpSession.getAttribute("app") == null) {
            model = reloadStep1(model, locale);
            return "index";
        } else {
            app = (ThyApplication) httpSession.getAttribute("app");
            model = reloadStep2(model, locale, app);
            return "step2";
        }
    }

    @PostMapping(value = "/step2", params = {"next"})
    public String postStep2(Model model, @ModelAttribute("app") ThyApplication requestApp, BindingResult result, HttpSession httpSession, Locale locale) {
        if (httpSession.getAttribute("app") == null) {
            model = reloadStep1(model, locale);
            return "index";
        } else {
            validator.validateStep2(requestApp, result);
            ThyApplication app = (ThyApplication) httpSession.getAttribute("app");
            if (result.hasErrors()) {
                model = reloadStep2(model, locale, app);
                return "step2";
            }
            app.setRegistrationCity(requestApp.getRegistrationCity());
            app.setRegistrationAddress(requestApp.getRegistrationAddress());
            app.setMobileNumber(requestApp.getMobileNumber());
            app.setEmail(requestApp.getEmail());
            app.setDomicileSame(requestApp.isDomicileSame());
            app.setDomicileAddress(requestApp.getDomicileAddress());
            System.out.println(app);
            app = mainService.saveApplication(app);
            httpSession.setAttribute("app", app);
            return "redirect:/step3";

        }
    }

    @PostMapping(value = "/step2", params = {"back"})
    public String postStep2Back(HttpSession httpSession) {
        if (httpSession.getAttribute("app") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("app");
            mainService.deleteApplication(sessionApp.getId());
            httpSession.removeAttribute("step2");
        }
        return "redirect:/index";
    }

    @GetMapping("/step3")
    public String showStep3(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        model.addAttribute("lcl", locale.getLanguage());
        if (httpSession.getAttribute("app") == null) {
            model = reloadStep1(model, locale);
            return "index";
        } else {
            app.setTkNoAvailable(true);
            model.addAttribute("isTkNoAvailable", true);
            return "step3";
        }
    }

    @PostMapping(value = "/step3", params = {"next"})
    public String postStep3(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("app");
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
        if (sessionApp != null) {
            sessionApp.setTkNoAvailable(app.isTkNoAvailable());
            sessionApp.setTkNo(app.getTkNo());
            sessionApp.setPassportName(app.getPassportName());
            sessionApp.setPassportSurname(app.getPassportSurname());
            mainService.saveApplication(sessionApp);
            httpSession.removeAttribute("app");
            httpSession.setAttribute("step4", sessionApp);
            return "redirect:/step4";
        } else {
            model = reloadStep1(model, locale);
            return "redirect:/index";
        }
    }

    @PostMapping(value = "/step3", params = {"back"})
    public String postStep3Back(HttpSession httpSession) {
        if (httpSession.getAttribute("app") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("app");
            mainService.deleteApplication(sessionApp.getId());
            httpSession.removeAttribute("app");
        }
        return "redirect:/index";
    }

    @GetMapping("/step4")
    public String showStep4(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        model.addAttribute("lcl", locale.getLanguage());
        if (httpSession.getAttribute("app") == null) {
            model = reloadStep1(model, locale);
            return "index";
        } else {
            return "step4";
        }
    }

    @PostMapping(value = "/step4", params = {"next"})
    public String postStep4(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        validator.validateStep4(app, result);
        if (result.hasErrors()) {
            model.addAttribute("lcl", locale.getLanguage());
            return "step4";
        }
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("app");
        if (sessionApp != null) {
            sessionApp.setUploadWrappers(app.getUploadWrappers());
            mainService.saveApplication(sessionApp);
            httpSession.removeAttribute("app");
            httpSession.setAttribute("app", sessionApp);
            return "redirect:/step5";
        } else {
            model = reloadStep1(model, locale);
            return "redirect:/index";
        }
    }

    @PostMapping(value = "/step4", params = {"back"})
    public String postStep4Back(HttpSession httpSession) {
        if (httpSession.getAttribute("app") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("app");
            mainService.deleteApplication(sessionApp.getId());
            httpSession.removeAttribute("app");
        }
        return "redirect:/index";
    }

    @GetMapping("/step5")
    public String showStep5(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        model.addAttribute("lcl", locale.getLanguage());
        if (httpSession.getAttribute("app") == null) {
            model = reloadStep1(model, locale);
            return "index";
        } else {
            return "step5";
        }
    }

    @PostMapping(value = "/step5", params = {"next"})
    public String postStep5(Model model, @ModelAttribute("app") ThyApplication app, BindingResult result, HttpSession httpSession, Locale locale) {
        validator.validateStep5(app, result);
        if (result.hasErrors()) {
            model.addAttribute("lcl", locale.getLanguage());
            return "step5";
        }
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("step5");
        if (sessionApp != null) {
            sessionApp.setCrsAnswers(app.getCrsAnswers());
            sessionApp.setAcceptedTerms(app.isAcceptedTerms());
            sessionApp.setAcceptedGsa(app.isAcceptedGsa());
            mainService.saveApplication(sessionApp);
            httpSession.removeAttribute("app");
            httpSession.setAttribute("app", sessionApp);
            return "redirect:/step6";
        } else {
            return "redirect:/index";
        }
    }

    @PostMapping(value = "/step5", params = {"back"})
    public String postStep5Back(HttpSession httpSession) {
        if (httpSession.getAttribute("app") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("app");
            mainService.deleteApplication(sessionApp.getId());
            httpSession.removeAttribute("app");
        }
        return "redirect:/index";
    }

    @GetMapping("/step6")
    public String showStep6(Model model, @ModelAttribute("app") ThyApplication app, HttpSession httpSession, Locale locale) {
        model.addAttribute("lcl", locale.getLanguage());
        if (httpSession.getAttribute("app") == null) {
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
        ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("app");
        if (sessionApp != null) {
            sessionApp.setCardProductId(app.getCardProductId());
            sessionApp.setBranchCode(app.getBranchCode());
            mainService.saveApplication(sessionApp);
            httpSession.removeAttribute("app");
            model.addAttribute("app", sessionApp);
            return "submit";
        } else {
            model = reloadStep1(model, locale);
            return "redirect:/index";
        }
    }

    @PostMapping(value = "/step6", params = {"back"})
    public String postStep6Back(HttpSession httpSession) {
        if (httpSession.getAttribute("app") != null) {
            ThyApplication sessionApp = (ThyApplication) httpSession.getAttribute("app");
            mainService.deleteApplication(sessionApp.getId());
            httpSession.removeAttribute("app");
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
