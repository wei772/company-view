package ee.idu.vc.util;

public class CVUtil {
    public static boolean isStringEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }

    public static boolean isAnyStringEmpty(String ... strings) {
        for (String string : strings) if (isStringEmpty(string)) return true;
        return false;
    }

    public static <T> T tolerantCast(Class<T> type, Object object) {
        return object == null ? null : type.cast(object);
    }
}