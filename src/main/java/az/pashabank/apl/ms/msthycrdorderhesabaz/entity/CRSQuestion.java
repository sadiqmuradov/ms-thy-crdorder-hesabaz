package az.pashabank.apl.ms.msthycrdorderhesabaz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "THY_CRS_QUESTIONS")
public class CRSQuestion {

    @Id
    private int id;
    private String lang;
    private String question;
    private String addquestion;
}
