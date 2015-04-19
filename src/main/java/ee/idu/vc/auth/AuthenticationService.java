package ee.idu.vc.auth;

import ch.qos.logback.classic.Logger;
import ee.idu.vc.controller.form.LoginForm;
import ee.idu.vc.model.Token;
import ee.idu.vc.model.Account;
import ee.idu.vc.repository.TokenRepository;
import ee.idu.vc.repository.AccountRepository;
import ee.idu.vc.util.CVUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
public class AuthenticationService {
    private static final long TOKEN_AGE_MS = 1000 * 60 * 60 * 24 * 7; // A week.

    private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public Token retrieveAccountToken(Account account) {
        Token token = tokenRepository.getMostRecent(account.getAccountId());
        if (token == null || exceedsMaxAge(token)) return tokenRepository.createFreshToken(account.getAccountId());
        token.setUpdateTime(CVUtil.currentTime());
        tokenRepository.updateToken(token);
        return token;
    }

    private boolean exceedsMaxAge(Token token) {
        long maxAge = TOKEN_AGE_MS + Calendar.getInstance().getTimeInMillis();
        return token.getUpdateTime().getTime() > maxAge;
    }

    public Account authAccount(LoginForm form) {
        if (form == null) return null;
        if (CVUtil.isAnyStringEmpty(form.getUsername(), form.getPassword())) return null;

        Account account = accountRepository.findByUsernameIgnoreCase(form.getUsername());
        if (account == null) return null;

        return BCrypt.checkpw(form.getPassword(), account.getPasswordHash()) ? account : null;
    }

    public Account authAccount(String username, String tokenString) {
        if (CVUtil.isAnyStringEmpty(username, tokenString)) return null;

        Account account = accountRepository.findByUsernameIgnoreCase(username);
        if (account == null) return null;

        Token token = tokenRepository.getMostRecent(account.getAccountId());
        if (!isTokenValid(token)) return null;

        return token.getUuid().equals(UUID.fromString(tokenString)) ? account : null;
    }

    private boolean isTokenValid(Token token) {
        return token != null && !exceedsMaxAge(token);
    }
}