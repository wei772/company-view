package ee.idu.vc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import ee.idu.vc.util.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class InternshipOffer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internshipOfferId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "internshipOfferStateId")
    private InternshipOfferState internshipOfferState;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creatorAccountId")
    private Account account;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private Timestamp expirationDate;

    private String title;
    private String content;

    public Long getInternshipOfferId() {
        return internshipOfferId;
    }

    public void setInternshipOfferId(Long internshipOfferId) {
        this.internshipOfferId = internshipOfferId;
    }

    public InternshipOfferState getInternshipOfferState() {
        return internshipOfferState;
    }

    public void setInternshipOfferState(InternshipOfferState internshipOfferState) {
        this.internshipOfferState = internshipOfferState;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}