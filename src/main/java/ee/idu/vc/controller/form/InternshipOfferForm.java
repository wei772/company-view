package ee.idu.vc.controller.form;

import ee.idu.vc.util.Constants;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class InternshipOfferForm {
    @NotEmpty(message = "Title cannot be empty.")
    @Size(max = 128, message = "Title is too long.")
    private String title;

    @DateTimeFormat(pattern = Constants.DATE_FORMAT)
    @Future(message = "Expiration date must be in the future.")
    @NotNull(message = "Expiration date must be set.")
    private Date expirationTime;

    @NotEmpty(message = "Content cannot be empty.")
    @Size(max = 65535, message = "Content is too long.")
    private String content;

    @NotNull(message = "Publishing status must be set.")
    private Boolean publish;

    @Override
    public String toString() {
        return "InternshipOfferForm{" +
                "title='" + title + '\'' +
                ", expirationTime='" + expirationTime + '\'' +
                ", content='" + content + '\'' +
                ", publish=" + publish +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean publish() { return publish; }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }
}