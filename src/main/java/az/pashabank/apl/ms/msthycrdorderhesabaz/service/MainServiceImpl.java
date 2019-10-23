package az.pashabank.apl.ms.msthycrdorderhesabaz.service;

import az.pashabank.apl.ms.msthycrdorderhesabaz.entity.ThyApplication;
import az.pashabank.apl.ms.msthycrdorderhesabaz.repository.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {

    @Autowired
    private Repositories repositories;

    public void newApplicationStep1(ThyApplication thyApplication){
        repositories.getThyApplicationRepo().save(thyApplication);
    }
}
