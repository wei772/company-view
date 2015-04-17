package ee.idu.vc.model;

import javax.persistence.*;

@Entity
public class AccountStatus {
    public static final String INACTIVE = "inactive", BANNED = "banned", ACTIVE = "active";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountStatusId;

    private String statusName;

    public Long getAccountStatusId() {
        return accountStatusId;
    }

    public void setAccountStatusId(Long accountStatusId) {
        this.accountStatusId = accountStatusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}