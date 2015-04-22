package ee.idu.vc.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class Traineeship implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long traineeshipId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "traineeshipStateId")
    private TraineeshipState traineeshipState;

    private String title;

    private String content;

    private Timestamp expirationDate;

    public Long getTraineeshipId() {
        return traineeshipId;
    }

    public void setTraineeshipId(Long traineeshipId) {
        this.traineeshipId = traineeshipId;
    }

    public TraineeshipState getTraineeshipState() {
        return traineeshipState;
    }

    public void setTraineeshipState(TraineeshipState traineeshipState) {
        this.traineeshipState = traineeshipState;
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
}