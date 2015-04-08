package ee.idu.vc.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AccountController {
    @RequestMapping(value = "/account/inbox", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView provideAccountInboxPage() { return new ModelAndView("angular"); }

    @RequestMapping(value = "/account/inbox/message", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView provideAccountInboxMessagePage() { return new ModelAndView("angular"); }

    @RequestMapping(value = "/account/password", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView providePasswordResetPage() {
        return new ModelAndView("angular");
    }

    @RequestMapping(value = "/account/details", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView provideAccountDetailsPage() {
        return new ModelAndView("angular");
    }
}
