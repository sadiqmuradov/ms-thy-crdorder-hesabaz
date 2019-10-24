package az.pashabank.apl.ms.msthycrdorderhesabaz.controller;

import az.pashabank.apl.ms.msthycrdorderhesabaz.entity.ThyApplication;
import az.pashabank.apl.ms.msthycrdorderhesabaz.logger.MainLogger;
import az.pashabank.apl.ms.msthycrdorderhesabaz.model.thy.RegisterCustomerInThyRequest;
import az.pashabank.apl.ms.msthycrdorderhesabaz.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
public class WebController {

    @Autowired
    private MainService mainService;

    private static final MainLogger LOGGER = MainLogger.getLogger(WebController.class);;

    @GetMapping(value = {"","/","/index"})
    public String getIndex(ModelMap model, HttpSession httpSession, Locale locale){
        mainService.newApplicationStep1(getMockData());
        return "index";
    }

    @PostMapping("registerCustomerInThy")
    public String registerCustomerInThy(
            @PathVariable final String lang,
            @RequestBody final RegisterCustomerInThyRequest request) {
        LOGGER.info("registerCustomerInThy. request: {}, lang {}", request, lang);
       // OperationResponse<RegisterCustomerInThyResponse> response = cardService.registerCustomerInThy(request);
       // LOGGER.info("registerCustomerInThy. response: {}", response);
        return "index";
    }

    private ThyApplication getMockData(){
        ThyApplication thyApplication = new ThyApplication();
        thyApplication.setName("Mahmud");
        thyApplication.setSurname("Aliyev");
        return thyApplication;
    }
}
