package az.pashabank.apl.ms.task;

import az.pashabank.apl.ms.dao.MainDao;
import az.pashabank.apl.ms.entity.CRSQuestion;
import az.pashabank.apl.ms.entity.ThyApplication;
import az.pashabank.apl.ms.entity.UploadWrapper;
import az.pashabank.apl.ms.enums.ResultCode;
import az.pashabank.apl.ms.logger.MainLogger;
import az.pashabank.apl.ms.model.OperationResponse;
import az.pashabank.apl.ms.model.Payment;
import az.pashabank.apl.ms.repository.Repositories;
import az.pashabank.apl.ms.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class OtrsScheduler {

    private static final MainLogger LOGGER = MainLogger.getLogger(OtrsScheduler.class);

    @Autowired
    private MainDao mainDao;

    @Autowired
    private MailService mailService;

    @Autowired
    private Repositories repositories;

    @Scheduled(fixedDelayString = "${schedule.fixedDelay.in.milliseconds.otrs}") // must be 180000
    public void sendCardOrderMails() {
        try {
            List<ThyApplication> unsentApplications = repositories.getThyApplicationRepo().findAllByPaymentCompletedTrueAndMailSentFalseOrderByCreateDate();
            List<String> emails = mainDao.getActiveMails();
            LOGGER.info("-----------------------------------------");
            LOGGER.info("Selected unsent applications and emails");
            for (ThyApplication app : unsentApplications) {
                emailUnsentApplication(app, emails);
            }
            LOGGER.info("-----------------------------------------");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void emailUnsentApplication(ThyApplication app, List<String> emails) {
        try {
            List<Resource> resources = new ArrayList<>();
            addFileUploads(app, resources);
            List<CRSQuestion> crsQuestions = repositories.getCrsQuestionRepo().findAllByLangIgnoreCase("en");
            Payment payment = repositories.getPaymentRepo().findPaymentByAppId(app.getId());

            OperationResponse operationResponse = mailService.sendEmail(app, crsQuestions, payment, emails, resources);
            if (operationResponse.getCode() == ResultCode.OK) {
                mainDao.markApplicationAsSent(app.getId());
//                validator.deleteWrapperFiles(app.getFileUploads(), operationResponse);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void addFileUploads(@NotNull ThyApplication app, List<Resource> resources) {
        for (UploadWrapper uw : app.getUploadWrappers()) {
            resources.add(new FileSystemResource(uw.getLocation()));
        }
    }

}
