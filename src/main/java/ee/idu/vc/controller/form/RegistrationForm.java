package ee.idu.vc.controller.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public class RegistrationForm {
    @NotEmpty(message = "First name cannot be empty.")
    @Size(max = 128, message = "First name is too long.")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty.")
    @Size(max = 128, message = "Last name is too long.")
    private String lastName;

    @NotEmpty(message = "Username cannot be empty.")
    @Size(min = 5, max = 48, message = "Username must be between 5 to 48 characters long.")
    private String username;

    @NotEmpty(message = "E-mail cannot be empty.")
    @Pattern(regexp = ".+\\@.+\\..+", message = "Invalid e-mail.")
    private String email;

    @NotEmpty(message = "Organisation name cannot be empty.")
    @Size(max = 200, message = "Organisation name is too long.")
    private String organisation;

    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 8, max = 32, message = "Password must be between 8 to 32 characters long.")
    private String password;

    @NotEmpty(message = "Telephone number cannot be empty.")
    @Size(max = 32, message = "Telephone number is too long.")
    private String telephone;

    @NotEmpty(message = "Address cannot be empty.")
    @Size(max = 300, message = "Address is too long.")
    private String address;

    private String emailConf;
    private String passwordConf;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailConf() {
        return emailConf;
    }

    public void setEmailConf(String emailConf) {
        this.emailConf = emailConf;
    }

    public String getPasswordConf() {
        return passwordConf;
    }

    public void setPasswordConf(String passwordConf) {
        this.passwordConf = passwordConf;
    }

    public boolean passwordsMatch() {
        return Objects.equals(getPassword(), getPasswordConf());
    }

    public boolean emailsMatch() {
        return Objects.equals(email, emailConf);
    }

    @Override
    public String toString() {
        return "RegistrationForm{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", emailConf='" + emailConf + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", organisation='" + organisation + '\'' +
                ", telephone='" + telephone + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", passwordConf='" + passwordConf + '\'' +
                '}';
    }
}
