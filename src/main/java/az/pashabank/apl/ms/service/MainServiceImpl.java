package az.pashabank.apl.ms.service;

import az.pashabank.apl.ms.entity.ThyApplication;
import az.pashabank.apl.ms.repository.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {

    @Autowired
    private Repositories repositories;

    public void saveApplication(ThyApplication thyApplication) {
        repositories.getThyApplicationRepo().save(thyApplication);
    }

    public void deleteApplication(int appId) {
        repositories.getThyApplicationRepo().deleteById(appId);
        // delete olmali deyil ancaq update
    }
}
