package ee.idu.vc.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Kasutaja")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kasutajaID")
    private Long userId;

    @Column(name = "eesnimi")
    private String firstName;

    @Column(name = "perekonnanimi")
    private String lastName;

    @Column(name = "konto_nimi", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "organisatsiooni_nimi")
    private String organisationName;

    @Column(name = "parool")
    private String password;

    @Column(name = "tel_nr")
    private String telephoneNumber;

    @Column(name = "aadress")
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kasutajaTyypID")
    private UserType userType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kontoStaatusID")
    private AccountStatus accountStatus;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public boolean isStatusName(String statusName) {
        return accountStatus.getStatusName().trim().equalsIgnoreCase(statusName);
    }

    public boolean isUserType(String userTypeName) {
        return userType.getTypeName().trim().equalsIgnoreCase(userTypeName);
    }

    public boolean isUserTypeNameAny(String... userTypeNames) {
        for (String userTypeName : userTypeNames) {
            if (userType.getTypeName().trim().equalsIgnoreCase(userTypeName)) return true;
        }
        return false;
    }

    public boolean isAccountStatusNameAny(String... userStatusNames) {
        for (String userStatusName : userStatusNames) {
            if (accountStatus.getStatusName().trim().equalsIgnoreCase(userStatusName)) return true;
        }
        return false;
    }
}