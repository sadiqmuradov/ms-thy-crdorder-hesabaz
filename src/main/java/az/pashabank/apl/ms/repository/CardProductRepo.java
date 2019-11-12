package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.CardProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardProductRepo extends JpaRepository<CardProduct, Integer> {

}
