package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.ThyApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThyApplicationRepo extends JpaRepository<ThyApplication, Integer> {

}
