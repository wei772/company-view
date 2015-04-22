package ee.idu.vc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class InternshipOfferState implements Serializable {
    public static String PUBLISHED = "published", UNPUBLISHED = "unpublished";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internshipOfferStateId;

    private String stateName;

    public Long getInternshipOfferStateId() {
        return internshipOfferStateId;
    }

    public void setInternshipOfferStateId(Long internshipOfferStateId) {
        this.internshipOfferStateId = internshipOfferStateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}