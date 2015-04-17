package ee.idu.vc.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import ee.idu.vc.auth.AuthenticationService;
import ee.idu.vc.forms.LoginForm;
import ee.idu.vc.model.AccountStatus;
import ee.idu.vc.model.Token;
import ee.idu.vc.model.Account;
import ee.idu.vc.util.CVUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {
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
        Account account = authService.authAccount(loginForm);
        if (account == null) return CVUtil.jsonSimpleFailureMessage("Invalid username or password.");
        if (account.statusEquals(AccountStatus.BANNED)) return CVUtil.jsonSimpleFailureMessage("Account is banned.");
        return jsonTokenMessage(authService.retrieveAccountToken(account));
    }

    private JsonNode jsonTokenMessage(Token token) {
        if (token == null) throw new IllegalArgumentException("Argument token cannot be null.");
        return JsonNodeFactory.instance.objectNode().put("success", true).put("token", token.getUuid().toString());
    }
}