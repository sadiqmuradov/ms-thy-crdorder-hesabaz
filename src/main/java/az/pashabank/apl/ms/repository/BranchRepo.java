package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.Branch;
import az.pashabank.apl.ms.entity.BranchId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepo extends JpaRepository<Branch, BranchId> {

    List<Branch> findAllByActiveTrueAndLangIgnoreCaseOrderByOrderby(String lang);
    Branch findBranchByCodeAndActiveTrueAndLangIgnoreCase(String code, String lang);
}
