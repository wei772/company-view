package ee.idu.vc.repository;

import ch.qos.logback.classic.Logger;
import ee.idu.vc.model.Account;
import ee.idu.vc.util.CVUtil;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ilike;

@Repository
public class HbnAccountRepository implements AccountRepository {
    private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @Qualifier("mainSessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Account findByEmailIgnoreCase(String email) {
        if (email == null) return null;
        Criteria criteria = currentSession().createCriteria(Account.class);
        criteria.add(ilike("email", email));
        try {
            return CVUtil.tolerantCast(Account.class, criteria.uniqueResult());
        } catch (NonUniqueResultException exception) {
            log.warn("There are over one users with same email in the database. Returning first result.", exception);
            return CVUtil.tolerantCast(Account.class, criteria.setMaxResults(1).uniqueResult());
        }
    }

    @Override
    public Account findByUsernameIgnoreCase(String username) {
        if (username == null) return null;
        Criteria criteria = currentSession().createCriteria(Account.class);
        criteria.add(ilike("username", username));
        try {
            return CVUtil.tolerantCast(Account.class, criteria.uniqueResult());
        } catch (NonUniqueResultException exception) {
            log.warn("There are over one users with same username in the database. Returning first result.", exception);
            return CVUtil.tolerantCast(Account.class, criteria.setMaxResults(1).uniqueResult());
        }
    }

    @Override
    public Account findByUsername(String username) {
        if (username == null) return null;
        Criteria criteria = currentSession().createCriteria(Account.class);
        criteria.add(eq("username", username));
        return CVUtil.tolerantCast(Account.class, criteria.uniqueResult());
    }

    @Override
    public Account findById(Long id) {
        if (id == null) return null;
        return CVUtil.tolerantCast(Account.class, currentSession().get(Account.class, id));
    }

    @Override
    public void create(Account user) {
        if (user == null) throw new IllegalArgumentException("Argument user cannot be null.");
        user.setAccountId(null);
        currentSession().save(user);
    }

    @Override
    public void update(Account user) {
        if (user == null) throw new IllegalArgumentException("Argument user cannot be null.");
        currentSession().update(user);
    }

    @Override
    public void delete(Account user) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Long id) {
        throw new NotImplementedException();
    }
}