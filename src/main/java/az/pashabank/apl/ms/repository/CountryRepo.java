package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepo extends JpaRepository<Country, Integer> {

    List<Country> findAllByLangIgnoreCaseOrderByName(String lang);
    Country findCountryByLangIgnoreCaseAndCode(String lang, String code);

}
