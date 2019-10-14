package az.pashabank.apl.ms.msthycrdorderhesabaz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
public class WebController {

    @GetMapping(value = {"","/","/index"})
    public String getIndex(ModelMap model, HttpSession httpSession, Locale locale){
        return "/";
    }
}
