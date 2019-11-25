package az.pashabank.apl.ms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "thy_coupon_codes")
public class CouponCode {

    @Id
    private int id;
    private int cardProductCode;
    private String couponCode;
    private int status;
    private int appid;
    private int couponPrice;
    private String serialNo;
    private String username;
    private String type;
    private String branchCode;

}
