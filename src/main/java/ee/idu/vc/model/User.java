package ee.idu.vc.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Kasutaja")
public class User implements Serializable {
    @Id
    @Column(name = "kasutajaID")
    private Long userId;

    @Column(name = "eesnimi")
    private String firstName;

    @Column(name = "perekonnanimi")
    private String lastName;

    @Column(name = "konto_nimi")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "organisatsiooni_nimi")
    private String organisationName;

    @Column(name = "password")
    private String password;

    @Column(name = "tel_nr")
    private String telephoneNumber;

    @Column(name = "aadress")
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kasutajaTyypID")
    private UserType userType;

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
}