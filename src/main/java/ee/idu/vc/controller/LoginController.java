package ee.idu.vc.controller;

import ee.idu.vc.model.Token;
import ee.idu.vc.service.AuthenticationService;
import ee.idu.vc.controller.response.JsonResponse;
import ee.idu.vc.controller.response.SimpleResponse;
import ee.idu.vc.controller.response.TokenResponse;
import ee.idu.vc.controller.form.LoginForm;
import ee.idu.vc.model.Account;
import ee.idu.vc.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {
    @Autowired
    private AuthenticationService authService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView angularView() {
        return new ModelAndView("angular");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResponse login(LoginForm form) {
        Account account = authService.loginWithCredentials(form.getUsername(), form.getPassword());
        if (account == null) return new SimpleResponse("Invalid username or password.");
        if (authService.isBanned(account)) return new SimpleResponse("Your account is banned.");

        Token token = tokenService.latestValidToken(account);
        if (token == null) token = tokenService.newToken(account);
        return new TokenResponse(token.getUuid().toString());
    }
}