package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepo extends JpaRepository<Branch, String> {

}
