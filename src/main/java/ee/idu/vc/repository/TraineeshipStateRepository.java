package ee.idu.vc.repository;

import ee.idu.vc.model.TraineeshipState;

import javax.transaction.Transactional;

public interface TraineeshipStateRepository {
    @Transactional
    public TraineeshipState findById(Long id);

    @Transactional
    public TraineeshipState findByName(String stateName);
}