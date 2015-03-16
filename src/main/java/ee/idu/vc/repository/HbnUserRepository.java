package ee.idu.vc.repository;

import ch.qos.logback.classic.Logger;
import ee.idu.vc.model.User;
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
public class HbnUserRepository implements UserRepository {
    private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @Qualifier("remoteDBSessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public User findByCredentials(String username, String password) {
        if (CVUtil.isAnyStringEmpty(username, password)) return null;

        Criteria criteria = currentSession().createCriteria(User.class);
        criteria.add(and(eq("username", username), eq("password", password)));
        User user = CVUtil.tolerantCast(User.class, criteria.uniqueResult());

        log.debug(user == null ? "Could not find user " + username + "." : "Found user " + username + ".");
        return user;
    }

    @Override
    public User findByEmailIgnoreCase(String email) {
        if (email == null) return null;
        Criteria criteria = currentSession().createCriteria(User.class);
        criteria.add(ilike("email", email));
        try {
            return CVUtil.tolerantCast(User.class, criteria.uniqueResult());
        } catch (NonUniqueResultException exception) {
            log.warn("There are over one users with same email in the database. Returning first result.", exception);
            return CVUtil.tolerantCast(User.class, criteria.setMaxResults(1).uniqueResult());
        }
    }

    @Override
    public User findByUsernameIgnoreCase(String username) {
        if (username == null) return null;
        Criteria criteria = currentSession().createCriteria(User.class);
        criteria.add(ilike("username", username));
        try {
            return CVUtil.tolerantCast(User.class, criteria.uniqueResult());
        } catch (NonUniqueResultException exception) {
            log.warn("There are over one users with same username in the database. Returning first result.", exception);
            return CVUtil.tolerantCast(User.class, criteria.setMaxResults(1).uniqueResult());
        }
    }

    @Override
    public User findById(Long id) {
        if (id == null) return null;
        return CVUtil.tolerantCast(User.class, currentSession().get(User.class, id));
    }

    @Override
    public void create(User user) {
        if (user == null) throw new IllegalArgumentException("Argument user cannot be null.");
        user.setUserId(null);
        currentSession().save(user);
    }

    @Override
    public void update(User user) {
        if (user == null) throw new IllegalArgumentException("Argument user cannot be null.");
        currentSession().update(user);
    }

    @Override
    public void delete(User user) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Long id) {
        throw new NotImplementedException();
    }
}