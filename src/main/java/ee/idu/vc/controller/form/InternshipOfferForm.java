package ee.idu.vc.controller.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InternshipOfferForm {
    @NotEmpty(message = "Title cannot be empty.")
    @Size(max = 128, message = "Title is too long.")
    private String title;

    @NotEmpty(message = "Expiration date must be set.")
    private String expirationTime;

    @NotEmpty(message = "Content cannot be empty.")
    @Size(max = 65535, message = "Content is too long.")
    private String content;

    @NotNull
    private boolean publish;

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

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }
}