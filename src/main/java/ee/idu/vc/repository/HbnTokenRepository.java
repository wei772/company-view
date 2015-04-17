package ee.idu.vc.repository;

import ch.qos.logback.classic.Logger;
import ee.idu.vc.model.Token;
import ee.idu.vc.util.CVUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
public class HbnTokenRepository implements TokenRepository {
    private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @Qualifier("mainSessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Token getMostRecent(Long accountId) {
        if (accountId == null) return null;
        Criteria criteria = currentSession().createCriteria(Token.class);
        criteria.add(eq("accountId", accountId)).addOrder(Order.desc("updateTime"));
        Token token = CVUtil.tolerantCast(Token.class, criteria.setMaxResults(1).uniqueResult());
        log.debug("Most recent token for user " + accountId + " is: " + (token == null ? "none" : token.getUuid()) + ".");
        return token;
    }

    @Override
    public Token createFreshToken(Long accountId) {
        if (accountId == null) throw new IllegalArgumentException("Argument accountId cannot be null.");
        Token token = new Token();
        token.setAccountId(accountId);
        token.setUuid(UUID.randomUUID());
        token.setUpdateTime(CVUtil.currentTime());
        currentSession().save(token);
        return token;
    }

    @Override
    public void updateToken(Token token) {
        currentSession().update(token);
    }
}