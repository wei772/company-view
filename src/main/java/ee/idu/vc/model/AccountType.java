package ee.idu.vc.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class AccountType implements Serializable {
    public static final String COMPANY = "company", MODERATOR = "moderator";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountTypeId;

    private String typeName;

    public Long getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Long accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}