package az.pashabank.apl.ms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.StringJoiner;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "THY_UPLOADS_HESABAZ")
public class UploadWrapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_THY_UPLOADS_HESABAZ")
    @SequenceGenerator(sequenceName = "SEQ_THY_UPLOADS_HESABAZ", allocationSize = 1, name = "SEQ_THY_UPLOADS_HESABAZ")
    @JsonIgnore
    private int id;
    private String name;
    private String location;
    private String contentType;
    private long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appId", nullable = false)
//    @JsonIgnore
    private ThyApplication app;

    @Override
    public String toString() {
        return new StringJoiner(", ", UploadWrapper.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("location='" + location + "'")
                .add("contentType='" + contentType + "'")
                .add("fileSize=" + fileSize)
                .toString();
    }
}
