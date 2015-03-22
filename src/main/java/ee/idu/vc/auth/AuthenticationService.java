package ee.idu.vc.auth;

import ch.qos.logback.classic.Logger;
import ee.idu.vc.forms.LoginForm;
import ee.idu.vc.model.AccountStatus;
import ee.idu.vc.model.Token;
import ee.idu.vc.model.User;
import ee.idu.vc.model.UserType;
import ee.idu.vc.repository.TokenRepository;
import ee.idu.vc.repository.UserRepository;
import ee.idu.vc.util.CVUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
public class AuthenticationService {
    private static final long TOKEN_AGE_MS = 1000 * 60 * 60 * 24 * 7; // A week.

    private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    /**
     * Checks if the user is allowed to authenticate.
     * @param user User to check. If null then exceptions are thrown.
     * @return Reason for not letting authenticate or null if allowed.
     */
    public String checkIfCanAuth(User user) {
        validateUser(user);
        if (user.isStatusName(AccountStatus.BANNED)) return "Account is banned.";
        if (!user.isUserType(UserType.COMPANY)) return "This is not a company account.";
        return null;
    }

    public Token retrieveUserToken(User user) {
        validateUser(user);
        Token token = tokenRepository.getMostRecent(user.getUserId());
        if (token == null || exceedsMaxAge(token)) return tokenRepository.createFreshToken(user.getUserId());
        token.setCreationDate(CVUtil.currentTime());
        tokenRepository.updateToken(token);
        return token;
    }

    private boolean exceedsMaxAge(Token token) {
        long maxAge = TOKEN_AGE_MS + Calendar.getInstance().getTimeInMillis();
        return token.getCreationDate().getTime() > maxAge;
    }

    /**
     * Gets the user by credentials(case sensitive).
     * @param loginForm Login form.
     * @return User or null if not found.
     */
    public User getUserIfExists(LoginForm loginForm) {
        if (loginForm == null) {
            log.warn("Login form is null.");
            return null;
        }
        return getUserIfExists(loginForm.getUsername(), loginForm.getPassword());
    }

    /**
     * Gets the user by credentials(case sensitive).
     * @param username Username.
     * @param password Password.
     * @return User or null if not found.
     */
    public User getUserIfExists(String username, String password) {
        if (CVUtil.isAnyStringEmpty(username, password)) return null;
        return userRepository.findByCredentials(username, password);
    }

    public User getUserMatchingRecentToken(String username, String tokenString) {
        if (CVUtil.isAnyStringEmpty(username, tokenString)) return null;

        User user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) return null;

        Token token = tokenRepository.getMostRecent(user.getUserId());
        if (token == null || exceedsMaxAge(token)) return null;
        if (!token.getToken().equals(UUID.fromString(tokenString))) return null;
        return user;
    }

    private void validateUser(User user) {
        if (user == null) throw new IllegalArgumentException("Argument user does not exist.");
        if (user.getAccountStatus() == null)
            throw new IllegalArgumentException("User argument \"accountStatus\" should never be null.");
        if (user.getUserType() == null)
            throw new IllegalArgumentException("User argument \"userType\" should never be null.");
        if (user.getUserId() == null)
            throw new IllegalArgumentException("User argument \"userId\" should never be null.");
    }
}