package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.ThyApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThyApplicationRepo extends JpaRepository<ThyApplication, Integer> {

    List<ThyApplication> findAllByPaymentCompletedTrueAndMailSentFalseOrderByCreateDate();
}
