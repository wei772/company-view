package ee.idu.vc.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtil {
    private static final Random random = new SecureRandom();
    private static final char[] ALPHA_NUMERICS = alphaNumericChars();

    private static char[] alphaNumericChars() {
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        return (letters + letters.toUpperCase() + numbers).toCharArray();
    }

    public static String randomAlphaNumeric(int length) {
        if (length < 0) throw new IllegalArgumentException("Length cannot be negative.");
        if (length == 0) return "";

        String result = "";
        while (result.length() < length) result += ALPHA_NUMERICS[random.nextInt(ALPHA_NUMERICS.length)];
        return result;
    }
}