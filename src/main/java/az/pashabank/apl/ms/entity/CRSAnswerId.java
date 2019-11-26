package az.pashabank.apl.ms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CRSAnswerId implements Serializable {

    private Integer questionId;
    private ThyApplication app;
}