package ee.idu.vc.repository;

import ee.idu.vc.model.TraineeshipState;
import ee.idu.vc.util.CVUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
public class HbnTraineeshipStateRepository implements TraineeshipStateRepository {
    @Qualifier("mainSessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public TraineeshipState findById(Long id) {
        return CVUtil.tolerantCast(TraineeshipState.class, currentSession().get(TraineeshipState.class, id));
    }

    @Override
    public TraineeshipState findByName(String stateName) {
        Criteria criteria = currentSession().createCriteria(TraineeshipState.class);
        criteria.add(eq("stateName", stateName));
        return CVUtil.tolerantCast(TraineeshipState.class, criteria.uniqueResult());
    }
}