package ee.idu.vc.controllers;

import ch.qos.logback.classic.Logger;
import ee.idu.vc.util.CVUtil;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    private static final String USER_REGEX = "^[a-zA-Z0-9]{5,32}$";
    private static final String PASS_REGEX = "^[a-zA-Z0-9]{8,32}$";

    private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password) {
        if (!validCredentials(username, password)) return unauthorized("Credentials are not valid.");
        return CVUtil.simpleJsonObject("username", username, "password", password);
    }

    private boolean validCredentials(String username, String password) {
        return !(username == null || password == null) && username.matches(USER_REGEX) && password.matches(PASS_REGEX);
    }

    private ResponseEntity<String> unauthorized(String message) {
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }
}