package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.CRSAnswer;
import az.pashabank.apl.ms.entity.CRSAnswerId;
import az.pashabank.apl.ms.entity.CRSQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CRSAnswerRepo extends JpaRepository<CRSAnswer, CRSAnswerId> {

        List<CRSAnswer> findAllByAppId(int appId);
}
