package ee.idu.vc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class InternshipApplicant implements Serializable {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internshipApplicantId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "internshipOfferId")
    private InternshipOffer internshipOffer;

    private Long studentId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getInternshipApplicantId() {
        return internshipApplicantId;
    }

    public void setInternshipApplicantId(Long internshipApplicantId) {
        this.internshipApplicantId = internshipApplicantId;
    }

    public InternshipOffer getInternshipOffer() {
        return internshipOffer;
    }

    public void setInternshipOffer(InternshipOffer internshipOffer) {
        this.internshipOffer = internshipOffer;
    }
}