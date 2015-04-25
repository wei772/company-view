package ee.idu.vc.controller;

import ee.idu.vc.auth.AuthAccount;
import ee.idu.vc.auth.RequireAuth;
import ee.idu.vc.controller.form.InternshipOfferForm;
import ee.idu.vc.controller.response.JsonResponse;
import ee.idu.vc.controller.response.SimpleResponse;
import ee.idu.vc.model.Account;
import ee.idu.vc.repository.AccountRepository;
import ee.idu.vc.service.AuthenticationService;
import ee.idu.vc.service.InternshipService;
import ee.idu.vc.util.CVUtil;
import ee.idu.vc.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class InternshipOfferController {
    @Autowired
    private InternshipService internshipService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = {"/offer/internship/your", "/offer/internship/new", "/offer/internship/all"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView angularView() { return new ModelAndView("angular"); }

    @RequireAuth
    @RequestMapping(value = "/offer/internship/search", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object search(@RequestParam(required = false) Integer page, @RequestParam(required = false) String username,
                         @RequestParam(required = false, defaultValue = "true") boolean onlyPublished,
                         @RequestParam(required = false) String keyword, @AuthAccount Account requester) {
        if (page == null || page < 1) page = 1;
        Account account = CVUtil.isStringEmpty(username) ? null : accountRepository.findByUsername(username, false);
        if (!requester.equals(account) && !onlyPublished && !authenticationService.isModerator(requester)) {
            return new ResponseEntity<>("Non-moderator accounts can only search other users published internship " +
                    "offers.", HttpStatus.UNAUTHORIZED);
        }
        return internshipService.searchInternships(calcFrom(page), calcTo(page), onlyPublished, account, keyword);
    }

    @RequireAuth
    @RequestMapping(value = "/offer/internship", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse add(@Validated InternshipOfferForm form, BindingResult bind, @AuthAccount Account account) {
        SimpleResponse response = new SimpleResponse(bind);
        if (!response.hasErrors()) internshipService.createAndSave(form, account);
        return response;
    }

    private int calcFrom(int pageNumber) {
        return (pageNumber - 1) * Constants.RESULTS_PER_PAGE;
    }

    private int calcTo(int pageNumber) {
        return pageNumber * Constants.RESULTS_PER_PAGE;
    }
}