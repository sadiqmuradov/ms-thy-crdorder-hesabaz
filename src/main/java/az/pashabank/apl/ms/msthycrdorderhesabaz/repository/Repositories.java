package az.pashabank.apl.ms.msthycrdorderhesabaz.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Repositories {
    @Autowired
    private ThyApplicationRepo thyApplicationRepo;
}
