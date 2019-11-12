package az.pashabank.apl.ms.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.sql.Date;

@Data
@Entity
@Table(name = "THY_CARD_PRODUCTS")
public class CardProduct {

    @Id
    private int id;
    private String name;
    private BigDecimal urgency;
    private Date createDate;
    private Date lastUpdate;
    private int active;
    @Transient
    private BigDecimal price;
}
