package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer> {

    Payment findPaymentByEcommTransAndStatus(String ecommTrans, int status);
    Payment findPaymentByAppIdAndStatus(int appId, int status);
    Payment findPaymentByAppId(int appId);
    List<Payment> findAllByStatusOrderByCreateDate(int status);
}
