package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepo extends JpaRepository<City, Integer> {

    List<City> findAllByCountryCodeIgnoreCaseOrderByName(String countryCode);
    City findCityByCountryCodeAndCode(String countryCode, String code);

}
