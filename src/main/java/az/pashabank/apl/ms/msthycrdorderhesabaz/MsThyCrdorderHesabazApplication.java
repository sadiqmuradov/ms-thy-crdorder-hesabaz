package az.pashabank.apl.ms.msthycrdorderhesabaz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("az.pashabank.apl.ms.msthycrdorderhesabaz.proxy")
public class MsThyCrdorderHesabazApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsThyCrdorderHesabazApplication.class, args);
    }

}
