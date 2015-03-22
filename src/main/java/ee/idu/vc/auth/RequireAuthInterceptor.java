package ee.idu.vc.auth;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.idu.vc.model.User;
import ee.idu.vc.util.CVUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequireAuthInterceptor extends HandlerInterceptorAdapter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String USERNAME = "username", TOKEN = "token";

    private final ObjectMapper jsonMapper = new ObjectMapper();
    { jsonMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); }

    private Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequireAuth authAnnotation = handlerMethod.getMethodAnnotation(RequireAuth.class);
            if (authAnnotation != null && !checkAuthAndProceed(req, res, authAnnotation)) return false;
        }
        return super.preHandle(req, res, handler);
    }

    private boolean checkAuthAndProceed(HttpServletRequest req, HttpServletResponse res, RequireAuth authAnnotation)
            throws Exception {
        validateAuthAnnotation(authAnnotation);

        String authHeader = getAuthHeader(req, res);
        if (authHeader == null) return sendAuthError(res, "Invalid authentication header.");

        Map authMap = extractAuthHeader(authHeader);
        if (authMap == null) return sendAuthError(res, "Could not map authorization header to JSON.");

        User user = authenticateUser(authMap, authAnnotation);
        if (user == null) return sendAuthError(res, "Failed to authenticate user.");

        String cannotAuthMessage = authenticationService.checkIfCanAuth(user);
        if (cannotAuthMessage != null) return sendAuthError(res, cannotAuthMessage);

        // TODO: User is not null, add to @AuthUser annotation.
        return true;
    }

    private User authenticateUser(Map authMap, RequireAuth authAnnotation) {
        String username = authMap.get(USERNAME).toString();
        String tokenString = authMap.get(TOKEN).toString();
        log.debug("Authenticating user " + username + " using token " + tokenString + ".");

        User user = authenticationService.getUserMatchingRecentToken(username, tokenString);
        if (user == null) return null;
        if (!user.isAccountStatusNameAny(authAnnotation.accountStatuses())) return null;
        if (!user.isUserTypeNameAny(authAnnotation.userTypes())) return null;
        return user;
    }

    private String getAuthHeader(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (CVUtil.isStringEmpty(authHeader)) {
            log.debug("Authentication header is empty or null.");
            return null;
        }
        return authHeader;
    }

    private Map extractAuthHeader(String authHeaderContent) {
        try {
            Map params = jsonMapper.readValue(authHeaderContent, HashMap.class);
            if (!params.containsKey(USERNAME) && !params.containsKey(TOKEN)) {
                log.warn("Authentication header json content doesn't contain all required fields.");
                return null;
            }
            return params;
        } catch (IOException exception) {
            log.error("Failed to map authorization header content.", exception);
        }
        return null;
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