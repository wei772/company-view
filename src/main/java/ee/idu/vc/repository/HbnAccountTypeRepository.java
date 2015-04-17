package ee.idu.vc.repository;

import ee.idu.vc.model.AccountType;
import ee.idu.vc.util.CVUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
public class HbnAccountTypeRepository implements AccountTypeRepository {
    @Qualifier("mainSessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public AccountType findById(Long id) {
        return CVUtil.tolerantCast(AccountType.class, currentSession().get(AccountType.class, id));
    }

    @Override
    public AccountType findByName(String typeName) {
        Criteria criteria = currentSession().createCriteria(AccountType.class);
        criteria.add(eq("typeName", typeName));
        return CVUtil.tolerantCast(AccountType.class, criteria.uniqueResult());
    }
}
