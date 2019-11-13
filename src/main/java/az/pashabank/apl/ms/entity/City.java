package az.pashabank.apl.ms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "thy_cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "thy_city_seq")
    @SequenceGenerator(sequenceName = "thy_city_seq", name = "thy_city_seq", allocationSize = 1)
    @JsonIgnore
    private long id;

    private String code;
    private String name;
    private String countryCode;
}
