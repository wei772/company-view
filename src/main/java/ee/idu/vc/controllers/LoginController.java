package ee.idu.vc.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import ee.idu.vc.forms.LoginForm;
import ee.idu.vc.model.AccountStatus;
import ee.idu.vc.model.Token;
import ee.idu.vc.model.User;
import ee.idu.vc.repository.TokenRepository;
import ee.idu.vc.repository.UserRepository;
import ee.idu.vc.util.CVUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;

@RestController
public class LoginController {
    private static final long TOKEN_MAX_AGE_MS = 1000 * 60 * 60 * 24 * 7; // A week.
    private static final String MSG_BANNED = "Account has been banned.";
    private static final String MSG_INVALID_LOGIN = "Invalid username or password.";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView provideRegistrationPage() {
        return new ModelAndView("angular");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object login(LoginForm loginForm) {
        User user = userRepository.findByCredentials(loginForm.getUsername(), loginForm.getPassword());
        if (user == null) return CVUtil.jsonSimpleFailureMessage(MSG_INVALID_LOGIN);
        if (user.isStatusName(AccountStatus.BANNED)) return CVUtil.jsonSimpleFailureMessage(MSG_BANNED);
        return jsonTokenMessage(handleTokenRetrieval(user));
    }

    private JsonNode jsonTokenMessage(Token userToken) {
        return JsonNodeFactory.instance.objectNode().put("success", true)
                .put("token", userToken.getToken().toString());
    }

    private Token handleTokenRetrieval(User user) {
        Token token = tokenRepository.getMostRecent(user.getUserId());
        if (token == null || exceedsMaxAge(token)) return tokenRepository.createFreshToken(user.getUserId());
        return token;
    }

    private boolean exceedsMaxAge(Token token) {
        long maxAge = TOKEN_MAX_AGE_MS + Calendar.getInstance().getTimeInMillis();
        return token.getCreationDate().getTime() > maxAge;
    }
}