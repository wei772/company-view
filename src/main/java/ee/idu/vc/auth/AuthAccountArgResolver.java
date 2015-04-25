package ee.idu.vc.auth;

import ch.qos.logback.classic.Logger;
import ee.idu.vc.model.Account;
import ee.idu.vc.service.AuthenticationService;
import ee.idu.vc.util.CVUtil;
import ee.idu.vc.util.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthAccountArgResolver implements HandlerMethodArgumentResolver {
    private Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationService authService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthAccount.class) != null;
    }

    @Override
    public Account resolveArgument(MethodParameter methodParameter, ModelAndViewContainer movContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (!hasMethodRequireAuthAnnotation(methodParameter)) return null;
        String authHeader = webRequest.getHeader((Constants.AUTHORIZATION_HEADER));
        if (CVUtil.isStringEmpty(authHeader)) return null;

        String username = CVUtil.readJsonField(authHeader, Constants.PARAM_USERNAME);
        String tokenString = CVUtil.readJsonField(authHeader, Constants.PARAM_TOKEN);
        if (username == null || tokenString == null) return null;

        return authService.loginWithToken(username, tokenString);
    }

    private boolean hasMethodRequireAuthAnnotation(MethodParameter parameter) {
        if (parameter.getMethodAnnotation(RequireAuth.class) != null) return true;
        log.warn("Using @AuthAccount requires using @RequireAuth annotation.");
        return false;
    }
}