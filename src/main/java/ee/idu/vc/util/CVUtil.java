package ee.idu.vc.util;

public class CVUtil {
    public static boolean isEmptyOrNotExisting(String text) {
        return text == null || text.trim().length() == 0;
    }
}
