package ee.idu.vc.controller;

import ee.idu.vc.auth.AuthenticationService;
import ee.idu.vc.controller.response.JsonResponse;
import ee.idu.vc.controller.response.SimpleResponse;
import ee.idu.vc.controller.response.TokenResponse;
import ee.idu.vc.controller.form.LoginForm;
import ee.idu.vc.model.AccountStatus;
import ee.idu.vc.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {
    @Autowired
    private AuthenticationService authService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getAngularView() {
        return new ModelAndView("angular");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResponse login(LoginForm loginForm) {
        Account account = authService.authAccount(loginForm);
        if (account == null) return new SimpleResponse("Invalid username or password.");
        if (account.statusEquals(AccountStatus.BANNED)) return new SimpleResponse("Your account is banned.");
        return new TokenResponse(authService.retrieveAccountToken(account).getUuid().toString());
    }
}