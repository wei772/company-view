package ee.idu.vc.service;

import ee.idu.vc.model.Account;
import ee.idu.vc.model.Token;
import ee.idu.vc.repository.TokenRepository;
import ee.idu.vc.util.CVUtil;
import ee.idu.vc.util.Constants;
import ee.idu.vc.util.HbnSessionProvider;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

import static org.hibernate.criterion.Restrictions.eq;

@Service
public class HbnTokenService extends HbnSessionProvider implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public Token latestToken(Account account) {
        if (account == null) throw new IllegalArgumentException("Argument account cannot be null.");
        Criteria criteria = currentSession().createCriteria(Token.class);
        criteria.add(eq("account", account)).addOrder(Order.desc("updateTime"));
        return CVUtil.tolerantCast(Token.class, criteria.setMaxResults(1).uniqueResult());
    }

    @Override
    public Token newToken(Account account) {
        if (account == null) throw new IllegalArgumentException("Argument account cannot be null.");
        Token token = new Token();
        token.setAccount(account);
        token.setUuid(UUID.randomUUID());
        token.setUpdateTime(CVUtil.currentTime());
        tokenRepository.save(token);
        return token;
    }

    @Override
    public void extendToken(Token token) {
        if (token == null) throw new IllegalArgumentException("Argument token cannot be null.");
        token.setUpdateTime(CVUtil.currentTime());
        tokenRepository.update(token);
    }

    @Override
    public Token latestValidToken(Account account) {
        Token token = latestToken(account);
        if (token == null) return null;
        return exceedsMaxAge(token) ? null : token;
    }

    private boolean exceedsMaxAge(Token token) {
        long maxAge = Constants.TOKEN_MAX_AGE_MS + Calendar.getInstance().getTimeInMillis();
        return token.getUpdateTime().getTime() > maxAge;
    }
}