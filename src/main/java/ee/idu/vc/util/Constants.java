package ee.idu.vc.util;

public class Constants {
    public static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";
    public static final int RESULTS_PER_PAGE = 20;
    public static final long TOKEN_MAX_AGE_MS = 1000 * 60 * 60 * 24 * 7;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String PARAM_USERNAME = "username", PARAM_TOKEN = "token";

    private Constants() {}
}