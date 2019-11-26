package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.CRSQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CRSQuestionRepo extends JpaRepository<CRSQuestion, Integer> {
        List<CRSQuestion> findAllByLangIgnoreCase(String lang);
}
