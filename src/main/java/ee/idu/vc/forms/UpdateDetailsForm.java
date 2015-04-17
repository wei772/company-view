package ee.idu.vc.forms;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UpdateDetailsForm {
    @NotEmpty(message = "First name cannot be empty.")
    @Size(max = 128, message = "First name is too long.")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty.")
    @Size(max = 128, message = "Last name is too long.")
    private String lastName;

    @NotEmpty(message = "E-mail cannot be empty.")
    @Pattern(regexp = ".+\\@.+\\..+", message = "Invalid e-mail.")
    private String email;

    @NotEmpty(message = "Organisation name cannot be empty.")
    @Size(max = 200, message = "Organisation name is too long.")
    private String organisation;

    @NotEmpty(message = "Telephone number cannot be empty.")
    @Size(max = 32, message = "Telephone number is too long.")
    private String telephone;

    @NotEmpty(message = "Address cannot be empty.")
    @Size(max = 300, message = "Address is too long.")
    private String address;

    private String emailConf;

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
}