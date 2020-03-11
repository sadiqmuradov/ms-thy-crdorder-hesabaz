package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.CardPrice;
import az.pashabank.apl.ms.entity.CardPriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardPriceRepo extends JpaRepository<CardPrice, CardPriceId> {

    CardPrice findCardPriceByCardProductIdAndPeriod(int cardProductId, int period);
}
