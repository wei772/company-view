package ee.idu.vc.repository;

import ch.qos.logback.classic.Logger;
import ee.idu.vc.model.Token;
import ee.idu.vc.model.User;
import ee.idu.vc.util.CVUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
public class HbnTokenRepository implements TokenRepository {
    private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @Qualifier("localDBSessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Token getMostRecent(Long userId) {
        if (userId == null) return null;

        Criteria criteria = currentSession().createCriteria(User.class);
        criteria.add(eq("userId", userId)).addOrder(Order.desc("creationDate"));
        Token token = CVUtil.tolerantCast(Token.class, criteria.setMaxResults(1).uniqueResult());

        log.debug("Most recent token for user " + userId + " is: " + (token == null ? "none" : token.getToken()) + ".");
        return token;
    }

    @Override
    public Token createFreshToken(Long userId) {
        if (userId == null) throw new IllegalArgumentException("Argument userId cannot be null.");
        Token token = new Token();
        token.setUserId(userId);
        token.setToken(UUID.randomUUID());
        token.setCreationDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        currentSession().save(token);
        return token;
    }
}