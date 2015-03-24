package ee.idu.vc.auth;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    private Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationService authService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthUser.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (!hasMethodRequireAuthAnnotation(parameter)) return null;
        Map authDetails = AuthUtil.extractAuthHeader(webRequest.getHeader(AuthUtil.AUTHORIZATION_HEADER));
        String username = authDetails.get(AuthUtil.USERNAME_KEY).toString();
        String tokenString = authDetails.get(AuthUtil.TOKEN_KEY).toString();
        return authService.getUserMatchingRecentToken(username, tokenString);
    }

    private boolean hasMethodRequireAuthAnnotation(MethodParameter parameter) {
        if (parameter.getMethodAnnotation(RequireAuth.class) == null) {
            log.warn("When using @AuthUser annotation then method must be " +
                    "annotated using the @RequireAuth annotation. Returning user as null.");
            return false;
        }
        return true;
    }
}