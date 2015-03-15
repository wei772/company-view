package ee.idu.vc.repository;

import ch.qos.logback.classic.Logger;
import ee.idu.vc.model.User;
import ee.idu.vc.util.CVUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;

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
    public User findById(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public void create(User repositoryObject) {
        throw new NotImplementedException();
    }

    @Override
    public void update(User repositoryObject) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(User repositoryObject) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Long id) {
        throw new NotImplementedException();
    }
}
