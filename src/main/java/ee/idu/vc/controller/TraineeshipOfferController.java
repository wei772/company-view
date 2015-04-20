package ee.idu.vc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class TraineeshipOfferController {
    @RequestMapping(value = {"/offer/traineeship", "/offer/traineeship/new"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getAngularView() { return new ModelAndView("angular"); }
}