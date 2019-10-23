package az.pashabank.apl.ms.msthycrdorderhesabaz.controller;

import az.pashabank.apl.ms.msthycrdorderhesabaz.entity.ThyApplication;
import az.pashabank.apl.ms.msthycrdorderhesabaz.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
public class WebController {

    @Autowired
    private MainService mainService;

    @GetMapping(value = {"","/","/index"})
    public String getIndex(ModelMap model, HttpSession httpSession, Locale locale){

        mainService.newApplicationStep1(getMockData());
        return "index";
    }

    private ThyApplication getMockData(){
        ThyApplication thyApplication = new ThyApplication();
        thyApplication.setName("Mahmud");
        thyApplication.setSurname("Aliyev");
        return thyApplication;
    }
}
