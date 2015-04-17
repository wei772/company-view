package ee.idu.vc.repository;

import ee.idu.vc.model.AccountStatus;
import ee.idu.vc.util.CVUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
public class HbnAccountStatusRepository implements AccountStatusRepository {
    @Qualifier("mainSessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public AccountStatus findById(Long id) {
        return CVUtil.tolerantCast(AccountStatus.class, currentSession().get(AccountStatus.class, id));
    }

    @Override
    public AccountStatus findByName(String statusName) {
        Criteria criteria = currentSession().createCriteria(AccountStatus.class);
        criteria.add(eq("statusName", statusName));
        return CVUtil.tolerantCast(AccountStatus.class, criteria.uniqueResult());
    }
}