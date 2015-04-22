package ee.idu.vc.repository;

import ee.idu.vc.model.Traineeship;
import ee.idu.vc.util.CVUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Repository
public class HbnTraineeshipRepository implements TraineeshipRepository {
    @Qualifier("mainSessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Traineeship findById(Long id) {
        if (id == null) throw new IllegalArgumentException("Argument id cannot be null.");
        return CVUtil.tolerantCast(Traineeship.class, currentSession().get(Traineeship.class, id));
    }

    @Override
    public void create(Traineeship traineeship) {
        if (traineeship == null) throw new IllegalArgumentException("Argument traineeship cannot be null.");
        traineeship.setTraineeshipId(null);
        currentSession().save(traineeship);
    }

    @Override
    public void update(Traineeship traineeship) {
        if (traineeship == null) throw new IllegalArgumentException("Argument traineeship cannot be null.");
        currentSession().update(traineeship);
    }

    @Override
    public void delete(Traineeship repositoryObject) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Long id) {
        throw new NotImplementedException();
    }
}
