package ee.idu.vc.auth;

import ee.idu.vc.model.Account;
import ee.idu.vc.service.AuthenticationService;
import ee.idu.vc.util.CVUtil;
import ee.idu.vc.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequireAuthInterceptor extends HandlerInterceptorAdapter {
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

    private boolean canProceed(HttpServletRequest httpRequest, HttpServletResponse httpResponse, RequireAuth reqAuth)
            throws Exception {
        String authHeader = httpRequest.getHeader(Constants.AUTHORIZATION_HEADER);
        if (CVUtil.isStringEmpty(authHeader)) {
            httpResponse.sendError(401, "Authentication header cannot be empty.");
            return false;
        }

        String username = CVUtil.readJsonField(authHeader, Constants.PARAM_USERNAME);
        String tokenUUID = CVUtil.readJsonField(authHeader, Constants.PARAM_TOKEN);
        if (username == null || tokenUUID == null) {
            httpResponse.sendError(401, "Authentication header doesn't contain username and/or token.");
            return false;
        }

        Account account = authService.loginWithToken(username, tokenUUID);
        if (account == null) {
            httpResponse.sendError(401, "Invalid username or token.");
            return false;
        }

        if (!meetsPrivileges(account, reqAuth)) {
            httpResponse.sendError(401, "Account " + username + " has no privileges to this resource.");
            return false;
        }

        return true;
    }

    private boolean meetsPrivileges(Account account, RequireAuth auth) {
        if (auth.requiredTypes() != null && auth.requiredTypes().length != 0) {
            for (String typeName : auth.requiredTypes()) {
                if (typeName.equals(account.getAccountType().getTypeName())) return true;
            }
            return false;
        }

        if (auth.requiredStatuses() != null && auth.requiredStatuses().length != 0) {
            for (String statusName : auth.requiredStatuses()) {
                if (statusName.equals(account.getAccountStatus().getStatusName())) return true;
            }
            return false;
        }

        // Auth header does not contain any status or type requirements so we can just return true.
        return true;
    }
}