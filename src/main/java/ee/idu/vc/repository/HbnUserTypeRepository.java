package ee.idu.vc.repository;

import ee.idu.vc.model.UserType;
import ee.idu.vc.util.CVUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
public class HbnUserTypeRepository implements UserTypeRepository {
    @Qualifier("remoteDBSessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public UserType findById(Long id) {
        return CVUtil.tolerantCast(UserType.class, currentSession().get(UserType.class, id));
    }

    @Override
    public UserType findByName(String typeName) {
        Criteria criteria = currentSession().createCriteria(UserType.class);
        criteria.add(eq("typeName", typeName));
        return CVUtil.tolerantCast(UserType.class, criteria.uniqueResult());
    }
}
