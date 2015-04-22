package ee.idu.vc.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CVUtil {
    public static final String DATE_FORMAT_STRING = "dd.MM.yyyy HH:mm";
    private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.ENGLISH);

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

    public static Timestamp currentTime() {
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }

    public static Date parseDateTime(String dateTime) {
        try {
            return dateFormat.parse(dateTime);
        } catch (Throwable throwable) {
            return null;
        }
    }

    public static Timestamp dateToTimestamp(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Integer parseInt(String integer) {
        try {
            return Integer.parseInt(integer);
        } catch (Throwable ignored) {
            return null;
        }
    }
}