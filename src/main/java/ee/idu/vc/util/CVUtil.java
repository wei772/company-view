package ee.idu.vc.util;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

public class CVUtil {
    private static final Logger log = (Logger) LoggerFactory.getLogger(CVUtil.class);
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    static { jsonMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); }

    public static <T> T readJsonField(String jsonObjContent, String key, Class<T> type) {
        try {
            return tolerantCast(type, jsonMapper.readValue(jsonObjContent, HashMap.class).get(key));
        } catch (IOException exception) {
            log.error("Failed to read json field from json object content: " + jsonObjContent, exception);
            return null;
        }
    }

    public static String readJsonField(String jsonObjContent, String key) {
        return readJsonField(jsonObjContent, key, String.class);
    }

    public static boolean isStringEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }

    public static <T> T tolerantCast(Class<T> type, Object object) {
        return object == null ? null : type.cast(object);
    }

    public static Timestamp currentTime() {
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }

    public static Timestamp toTimestamp(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
}