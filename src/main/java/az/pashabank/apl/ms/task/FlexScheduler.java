package az.pashabank.apl.ms.task;

import az.pashabank.apl.ms.dao.MainDao;
import az.pashabank.apl.ms.enums.ResultCode;
import az.pashabank.apl.ms.logger.MainLogger;
import az.pashabank.apl.ms.model.OperationResponse;
import az.pashabank.apl.ms.model.Payment;
import az.pashabank.apl.ms.service.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlexScheduler {

    private static final MainLogger LOGGER = MainLogger.getLogger(FlexScheduler.class);

    @Autowired
    private MainDao mainDao;

    @Autowired
    private MainServiceImpl mainService;
    
    @Scheduled(fixedDelayString = "${schedule.fixedDelay.in.milliseconds.flex}") // must be 120000
    public void makeFlexPayments() {
        try {
            List<Payment> unpaidFlexPayments = mainService.getUnpaidFlexPayments();
            LOGGER.info("-----------------------------------------");
            LOGGER.info("Selected unpaid card order payments in FLEX");
            for(Payment payment: unpaidFlexPayments) {
                OperationResponse<String> operationResponse = mainDao.makePaymentToFlex(payment.getId());
                if (operationResponse.getCode() == ResultCode.OK) {
                    LOGGER.info("After card order makePaymentToFlex. [SUCCESS] Flex payment is successful. trnRefNo: {}", operationResponse.getData());
                } else {
                    LOGGER.info("After card order makePaymentToFlex. [ERROR] Flex payment is failed.");
                }
            }
            LOGGER.info("-----------------------------------------");
        }
        catch(Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
