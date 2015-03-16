package ee.idu.vc.model;

import javax.persistence.*;

@Entity
@Table(name = "`KontoSyndmuseTyyp`")
public class AccountActionType {
    public static final String CREATION = "account creation", CONFIRMATION = "account confirmation",
            BANNING = "account banning", UNBANNING = "account unbanning", ACTIVATION = "account activation",
            DEACTIVATION = "account deactivation";

    @Id
    @GeneratedValue
    @Column(name = "`kontoSyndmuseTyypID`")
    private Long typeId;

    @Column(name = "`nimetus`")
    private String typeName;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}