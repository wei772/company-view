package ee.idu.vc.controller.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class UpdatePasswordForm {
    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 8, max = 32, message = "Password must be between 8 to 32 characters long.")
    private String password;

    @NotEmpty(message = "New password cannot be empty.")
    @Size(min = 8, max = 32, message = "New password must be between 8 to 32 characters long.")
    private String newPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}