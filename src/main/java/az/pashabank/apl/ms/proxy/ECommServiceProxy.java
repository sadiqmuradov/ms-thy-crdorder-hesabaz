package az.pashabank.apl.ms.proxy;

import az.pashabank.apl.ms.constants.URL;
import az.pashabank.apl.ms.model.ecomm.ECommRegisterPaymentRequest;
import az.pashabank.apl.ms.model.ecomm.ECommRegisterPaymentResponse;
import az.pashabank.apl.ms.model.ecomm.EcommPaymentStatusRequest;
import az.pashabank.apl.ms.model.ecomm.EcommPaymentStatusResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "ms-ecomm", url = "${ecomm.rest.endpoint}")
public interface ECommServiceProxy {

    @RequestMapping(method = RequestMethod.POST, value = URL.ECOMM_REGISTER_PAYMENT, consumes = "application/json", produces = "application/json")
    ECommRegisterPaymentResponse registerPayment(@RequestBody ECommRegisterPaymentRequest eCommRegisterPaymentRequest);

    @RequestMapping(method = RequestMethod.POST, value = URL.ECOMM_GET_PAYMENT_STATUS, consumes = "application/json", produces = "application/json")
    EcommPaymentStatusResponse getPaymentStatus(@PathVariable("lang") String lang, @RequestBody EcommPaymentStatusRequest ecommPaymentStatusRequest);
}

