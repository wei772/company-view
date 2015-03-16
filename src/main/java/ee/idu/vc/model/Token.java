package ee.idu.vc.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "Token")
public class Token implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "tokenId")
    private Long tokenId;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "token")
    private UUID token;

    @Column(name = "creationDate")
    private Timestamp creationDate;

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
