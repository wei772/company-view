package ee.idu.vc.model;

import javax.persistence.*;

@Entity
@Table(name = "KasutajaTyyp")
public class UserType {
    public static final String COMPANY = "company", STUDENT = "student", UNI_STAFF = "university staff";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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