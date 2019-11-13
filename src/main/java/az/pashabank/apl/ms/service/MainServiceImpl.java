package az.pashabank.apl.ms.service;

import az.pashabank.apl.ms.entity.CRSQuestion;
import az.pashabank.apl.ms.entity.City;
import az.pashabank.apl.ms.entity.Country;
import az.pashabank.apl.ms.entity.ThyApplication;
import az.pashabank.apl.ms.repository.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainServiceImpl implements MainService {

    @Autowired
    private Repositories repositories;

    public ThyApplication saveApplication(ThyApplication thyApplication) {
        return repositories.getThyApplicationRepo().save(thyApplication);
    }

    public void deleteApplication(int appId) {
        repositories.getThyApplicationRepo().deleteById(appId);
        // delete olmali deyil ancaq update
    }

    public List<Country> getCountryList(String lang) {
        return repositories.getCountryRepo().findAllByLangIgnoreCaseOrderByName(lang);
    }

    public List<City> getCityList(String countryCode) {
        return repositories.getCityRepo().findAllByCountryCodeIgnoreCaseOrderByName(countryCode);
    }

    public List<CRSQuestion> getCRSQuestionList(String lang) {
        return repositories.getCrsQuestionRepo().findAllByLang(lang.toLowerCase());
    }
}
