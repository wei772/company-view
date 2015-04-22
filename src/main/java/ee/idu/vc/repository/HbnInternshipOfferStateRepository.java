package ee.idu.vc.repository;

import ee.idu.vc.model.InternshipOfferState;
import ee.idu.vc.util.CVUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
public class HbnInternshipOfferStateRepository implements InternshipOfferStateRepository {
    @Qualifier("mainSessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public InternshipOfferState findById(Long id) {
        return CVUtil.tolerantCast(InternshipOfferState.class, currentSession().get(InternshipOfferState.class, id));
    }

    @Override
    public InternshipOfferState findByName(String stateName) {
        Criteria criteria = currentSession().createCriteria(InternshipOfferState.class);
        criteria.add(eq("stateName", stateName));
        return CVUtil.tolerantCast(InternshipOfferState.class, criteria.uniqueResult());
    }
}