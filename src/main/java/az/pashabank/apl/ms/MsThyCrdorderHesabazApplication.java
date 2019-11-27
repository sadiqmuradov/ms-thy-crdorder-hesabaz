package az.pashabank.apl.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients("az.pashabank.apl.ms.proxy")
@EnableScheduling
public class MsThyCrdorderHesabazApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsThyCrdorderHesabazApplication.class, args);
    }

}
