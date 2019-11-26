package az.pashabank.apl.ms.repository;

import az.pashabank.apl.ms.entity.UploadWrapper;
import az.pashabank.apl.ms.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadWrapperRepo extends JpaRepository<UploadWrapper, Integer> {

    List<UploadWrapper> findAllByAppId(int appId);
}
