package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.CardPrice;
import az.pashabank.apl.ms.entity.CardPriceId;
import az.pashabank.apl.ms.entity.CouponCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponCodeRepo extends JpaRepository<CouponCode, Integer> {

    CouponCode findCouponCodeByAppid(int appId);
}
