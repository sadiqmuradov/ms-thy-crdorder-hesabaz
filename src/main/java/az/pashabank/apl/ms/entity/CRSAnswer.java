package az.pashabank.apl.ms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.StringJoiner;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "THY_CRS_ANSWERS_HESABAZ")
@IdClass(CRSAnswerId.class)
public class CRSAnswer {

    @Id
    private Integer questionId;
    private Integer answer;
    private String description;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appId", nullable = false)
    private ThyApplication app;

    @Override
    public String toString() {
        return new StringJoiner(", ", CRSAnswer.class.getSimpleName() + "[", "]")
                .add("questionId=" + questionId)
                .add("answer=" + answer)
                .add("description='" + description + "'")
                .toString();
    }
}
