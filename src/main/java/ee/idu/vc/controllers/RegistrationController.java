package ee.idu.vc.controllers;

import ee.idu.vc.forms.RegistrationForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RegistrationController {
    //private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Object register(RegistrationForm registrationForm) {
        //TODO Feature not done yet.
        return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView provideRegistrationPage() {
        return new ModelAndView("angular");
    }
}
