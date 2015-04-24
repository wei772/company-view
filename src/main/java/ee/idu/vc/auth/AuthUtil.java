package ee.idu.vc.auth;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String USERNAME_KEY = "username", TOKEN_KEY = "token";

    private static final Logger log = (Logger) LoggerFactory.getLogger(AuthUtil.class);

    private static final ObjectMapper jsonMapper = new ObjectMapper();
    static { jsonMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); }

    public static Map extractAuthHeader(String authHeaderContent) {
        try {
            Map params = jsonMapper.readValue(authHeaderContent, HashMap.class);
            if (!params.containsKey(USERNAME_KEY) && !params.containsKey(TOKEN_KEY)) {
                log.warn("Authentication header json content doesn't contain all required fields.");
                return null;
            }
            return params;
        } catch (IOException exception) {
            log.error("Failed to map authorization header content.", exception);
        }
        return null;
    }
}