package ee.idu.vc.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import ee.idu.vc.auth.AuthenticationService;
import ee.idu.vc.forms.LoginForm;
import ee.idu.vc.model.Token;
import ee.idu.vc.model.User;
import ee.idu.vc.util.CVUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {
    private static final String MSG_INVALID_LOGIN = "Invalid username or password.";

    @Autowired
    private AuthenticationService authService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView provideRegistrationPage() {
        return new ModelAndView("angular");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object login(LoginForm loginForm) {
        User user = authService.getUserIfExists(loginForm);
        if (user == null) return CVUtil.jsonSimpleFailureMessage(MSG_INVALID_LOGIN);

        String authError = authService.checkIfCanAuth(user);
        if (authError != null) return CVUtil.jsonSimpleFailureMessage(authError);

        return jsonTokenMessage(authService.retrieveUserToken(user));
    }

    private JsonNode jsonTokenMessage(Token userToken) {
        if (userToken == null) throw new IllegalArgumentException("Argument userToken cannot be null.");
        return JsonNodeFactory.instance.objectNode().put("success", true)
                .put("token", userToken.getToken().toString());
    }
}