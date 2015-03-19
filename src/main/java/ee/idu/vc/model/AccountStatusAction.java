package ee.idu.vc.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "KontoSyndmus")
public class AccountStatusAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kontoSyndmusID")
    private Long accountActionId;

    @Column(name = "loodud")
    private Timestamp created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kontoSyndmuseTyypID")
    private AccountActionType accountActionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kasutajaID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vana_staatus")
    private AccountStatus oldAccountStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uus_staatus")
    private AccountStatus newAccountStatus;

    public Long getAccountActionId() {
        return accountActionId;
    }

    public void setAccountActionId(Long accountActionId) {
        this.accountActionId = accountActionId;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public AccountActionType getAccountActionType() {
        return accountActionType;
    }

    public void setAccountActionType(AccountActionType accountActionType) {
        this.accountActionType = accountActionType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AccountStatus getOldAccountStatus() {
        return oldAccountStatus;
    }

    public void setOldAccountStatus(AccountStatus oldAccountStatus) {
        this.oldAccountStatus = oldAccountStatus;
    }

    public AccountStatus getNewAccountStatus() {
        return newAccountStatus;
    }

    public void setNewAccountStatus(AccountStatus newAccountStatus) {
        this.newAccountStatus = newAccountStatus;
    }
}