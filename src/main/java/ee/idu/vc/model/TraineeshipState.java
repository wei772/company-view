package ee.idu.vc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class TraineeshipState implements Serializable {
    public static String PUBLISHED = "published", UNPUBLISHED = "unpublished";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long traineeshipStateId;

    private String stateName;

    public Long getTraineeshipStateId() {
        return traineeshipStateId;
    }

    public void setTraineeshipStateId(Long traineeshipStateId) {
        this.traineeshipStateId = traineeshipStateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}