package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.SourceOfIncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceOfIncomeRepo extends JpaRepository<SourceOfIncomeEntity, Integer> {

    List<SourceOfIncomeEntity> findSourceOfIncomeEntitiesByLangIgnoreCaseOrderById(String lang);
}
