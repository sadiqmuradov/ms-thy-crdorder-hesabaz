package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.NetGrossIncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetGrossIncomeRepo extends JpaRepository<NetGrossIncomeEntity, Integer> {

    List<NetGrossIncomeEntity> findNetGrossIncomeEntitiesByLangIgnoreCaseOrderById(String lang);
}
