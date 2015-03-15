package ee.idu.vc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KasutajaTyyp")
public class UserType {
    @Id
    @Column(name = "kasutajaTyypID")
    private Long userTypeId;

    @Column(name = "nimetus")
    private String typeName;

    public Long getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Long userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}