package ee.idu.vc.model;

import javax.persistence.*;

@Entity
@Table(name = "KontoStaatus")
public class AccountStatus {
    public static final String INACTIVE = "inactive", BANNED = "banned", ACTIVE = "active";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kontoStaatusID")
    private Long statusId;

    @Column(name = "nimetus")
    private String statusName;

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
