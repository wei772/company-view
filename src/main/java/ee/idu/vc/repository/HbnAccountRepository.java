package ee.idu.vc.repository;

import ch.qos.logback.classic.Logger;
import ee.idu.vc.model.Account;
import ee.idu.vc.repository.util.HbnCRUDRepository;
import ee.idu.vc.util.CVUtil;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
public class HbnAccountRepository extends HbnCRUDRepository<Account> implements AccountRepository {
    private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

    public HbnAccountRepository() {
        super(Account.class);
    }

    @Override
    public Account findByEmail(String email, boolean caseSensitive) {
        if (email == null) return null;
        Criteria criteria = createFieldEqualsCriteria("email", email, caseSensitive);
        try {
            return CVUtil.tolerantCast(Account.class, criteria.uniqueResult());
        } catch (NonUniqueResultException ignored) {
            logOverOneEmails(caseSensitive, email);
            return CVUtil.tolerantCast(Account.class, criteria.setMaxResults(1).uniqueResult());
        }
    }

    @Override
    public Account findByUsername(String username, boolean caseSensitive) {
        if (username == null) return null;
        Criteria criteria = createFieldEqualsCriteria("username", username, caseSensitive);
        try {
            return CVUtil.tolerantCast(Account.class, criteria.uniqueResult());
        } catch (NonUniqueResultException ignored) {
            logOverOneUsers(caseSensitive, username);
            return CVUtil.tolerantCast(Account.class, criteria.setMaxResults(1).uniqueResult());
        }
    }

    private Criteria createFieldEqualsCriteria(String fieldName, String fieldValue, boolean caseSensitive) {
        fieldValue = fieldValue.trim();
        Criteria criteria = currentSession().createCriteria(Account.class);
        criteria.add(caseSensitive ? eq(fieldName, fieldValue) : eq(fieldName, fieldValue).ignoreCase());
        return criteria;
    }

    private void logOverOneEmails(boolean caseSensitive, String email) {
        log.warn("There are over one accounts with the same email(" + email + ") in the repository " +
                "(case sensitivity = " + caseSensitive + ")");
    }

    private void logOverOneUsers(boolean caseSensitive, String username) {
        log.warn("There are over one accounts with the same username(" + username + ") in the repository " +
                "(case sensitivity = " + caseSensitive + ")");
    }
}