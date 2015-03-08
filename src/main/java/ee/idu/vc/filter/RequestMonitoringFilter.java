package ee.idu.vc.filter;

import ch.qos.logback.classic.Logger;
import ee.idu.vc.util.RandomUtil;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestMonitoringFilter extends GenericFilterBean {
    private static final String[][] BAD_ESCAPE_CHARS = {{"\t", "\\t"}, {"\b", "\\b"}, {"\n", "\\n"}, {"\r", "\\r"}};

    private final Logger log = (Logger) LoggerFactory.getLogger(getClass());
    private final AtomicInteger requestIndex = new AtomicInteger();
    private final String nodeName = RandomUtil.randomAlphaNumeric(4);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        Thread.currentThread().setName(nodeName + "-" + requestIndex.incrementAndGet());

        String requestInfo = getRequestInfo((HttpServletRequest) request);
        long startTime = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
            log.info("[" + deltaFrom(startTime) + " ms] served " + requestInfo);
        } catch (Throwable throwable) {
            log.error("[" + deltaFrom(startTime) + " ms] failed to serve " + requestInfo, throwable);
        }
    }

    private String getRequestInfo(HttpServletRequest request) {
        return request.getRemoteAddr() + " " + request.getMethod() + " " + normalizeURI(request);
    }

    private String normalizeURI(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder(normalize(request.getRequestURI()) + "?");
        for (Object param : request.getParameterMap().keySet()) {
            String params = Arrays.toString(request.getParameterValues(param.toString()));
            builder.append(normalize(param.toString())).append("=");
            builder.append(normalize(params)).append("&");
        }
        return builder.substring(0, builder.length() - 1);
    }

    private String normalize(String string) {
        for (String[] replacement : BAD_ESCAPE_CHARS) string = string.replace(replacement[0], replacement[1]);
        return string;
    }

    private long deltaFrom(long startTime) {
        return System.currentTimeMillis() - startTime;
    }
}