package ee.idu.vc.auth;

import ch.qos.logback.classic.Logger;
import ee.idu.vc.model.User;
import ee.idu.vc.util.CVUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RequireAuthInterceptor extends HandlerInterceptorAdapter {
    private Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationService authService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequireAuth authAnnotation = handlerMethod.getMethodAnnotation(RequireAuth.class);
            if (authAnnotation != null && !canProceed(req, res, authAnnotation)) return false;
        }
        return super.preHandle(req, res, handler);
    }

    private boolean canProceed(HttpServletRequest req, HttpServletResponse res, RequireAuth authAnnotation)
            throws Exception {
        validateAuthAnnotation(authAnnotation);

        String authHeader = getAuthHeader(req);
        if (authHeader == null) return sendAuthError(res, "Invalid authentication header.");

        Map authDetails = AuthUtil.extractAuthHeader(authHeader);
        if (authDetails == null) return sendAuthError(res, "Could not map authorization header to JSON.");

        User user = authenticateUser(authDetails, authAnnotation);
        if (user == null) return sendAuthError(res, "Failed to authenticate user.");

        String cannotAuthMessage = authService.checkIfCanAuth(user);
        if (cannotAuthMessage != null) return sendAuthError(res, cannotAuthMessage);
        return true;
    }

    private User authenticateUser(Map authDetails, RequireAuth authAnnotation) {
        String username = authDetails.get(AuthUtil.USERNAME_KEY).toString();
        String tokenString = authDetails.get(AuthUtil.TOKEN_KEY).toString();
        log.debug("Authenticating user " + username + " using token " + tokenString + ".");

        User user = authService.getUserMatchingRecentToken(username, tokenString);
        if (user == null) return null;
        if (!user.isAccountStatusNameAny(authAnnotation.accountStatuses())) return null;
        if (!user.isUserTypeNameAny(authAnnotation.userTypes())) return null;
        return user;
    }

    private String getAuthHeader(HttpServletRequest request) throws Exception {
        String authHeader = request.getHeader(AuthUtil.AUTHORIZATION_HEADER);
        if (CVUtil.isStringEmpty(authHeader)) {
            log.debug("Authentication header is empty or null.");
            return null;
        }
        return authHeader;
    }

    private void validateAuthAnnotation(RequireAuth requireAuth) {
        if (requireAuth.userTypes() == null || requireAuth.userTypes().length == 0)
            throw new IllegalArgumentException("Auth annotation \"userTypes\" argument cannot be null or empty.");

        if (requireAuth.accountStatuses() == null || requireAuth.accountStatuses().length == 0)
            throw new IllegalArgumentException("Auth annotation \"accountStatuses\" argument cannot be null or empty.");
    }

    private boolean sendAuthError(HttpServletResponse response, String message) throws IOException {
        response.sendError(401, message);
        return false;
    }
}