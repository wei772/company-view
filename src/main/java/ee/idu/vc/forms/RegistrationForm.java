package ee.idu.vc.forms;

public class RegistrationForm {
    private String username;
    private String email;
    private String emailConf;
    private String firstName;
    private String lastName;
    private String organisation;
    private String telephone;
    private String address;
    private String password;
    private String passwordConf;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailConf() {
        return emailConf;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConf() {
        return passwordConf;
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
