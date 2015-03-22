package ee.idu.vc.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class OfferController {
    @RequestMapping(value = "/offers", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView provideOffersPage() {
        return new ModelAndView("angular");
    }
}
