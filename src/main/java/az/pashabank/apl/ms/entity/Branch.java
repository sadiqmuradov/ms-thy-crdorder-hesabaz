package az.pashabank.apl.ms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class Branch {

    @Id
    private String branchCode;
    private String branchCity;
    private int orderby;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private String descr;

    public Branch(String branchCode, String name) {
        this.branchCode = branchCode;
        this.name = name;
    }
}
