package az.pashabank.apl.ms.service;

import az.pashabank.apl.ms.entity.City;
import az.pashabank.apl.ms.entity.Country;
import az.pashabank.apl.ms.entity.ThyApplication;

import java.util.List;

public interface MainService {
    ThyApplication saveApplication(ThyApplication thyApplication);
    List<Country> getCountryList(String lang);
    List<City> getCityList(String countryCode);
}
