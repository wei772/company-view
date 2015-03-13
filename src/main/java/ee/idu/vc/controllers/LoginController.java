package ee.idu.vc.controllers;

import ee.idu.vc.forms.LoginForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object login(LoginForm loginForm) {
        // TODO: looks like there are no errors, lets check db
        return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
    }
}