package ee.idu.vc.controller;

import ee.idu.vc.auth.AuthAccount;
import ee.idu.vc.auth.RequireAuth;
import ee.idu.vc.controller.form.InternshipOfferForm;
import ee.idu.vc.controller.response.JsonResponse;
import ee.idu.vc.controller.response.NewItemResponse;
import ee.idu.vc.controller.response.SimpleResponse;
import ee.idu.vc.model.Account;
import ee.idu.vc.model.InternshipOffer;
import ee.idu.vc.model.InternshipOfferState;
import ee.idu.vc.repository.AccountRepository;
import ee.idu.vc.repository.InternshipOfferRepository;
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
    private InternshipOfferRepository internshipOfferRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = {"/offer/internship/your", "/offer/internship/new", "/offer/internship/all",
            "/offer/internship/edit/*", "/offer/internship/view/*"}, method = RequestMethod.GET)
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
        if (response.hasErrors()) return response;
        Long internshipOfferId = internshipService.createAndSave(form, account).getInternshipOfferId();
        return new NewItemResponse(internshipOfferId);
    }

    @RequireAuth
    @RequestMapping(value = "/offer/internship", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object internship(@RequestParam Long id, @AuthAccount Account account) {
        InternshipOffer offer = internshipOfferRepository.findById(id);
        if (offer == null) return new ResponseEntity<>("Internship with id " + id + " doesn't exist.", HttpStatus.NOT_FOUND);
        if (isPublished(offer)) return offer;
        if (!account.equals(offer.getAccount())) return new ResponseEntity<>("It is forbidden to view other users " +
                "unpublished offers.", HttpStatus.UNAUTHORIZED);
        return offer;
    }

    @RequireAuth
    @RequestMapping(value = "/offer/internship", method = RequestMethod.PUT)
    @ResponseBody
    public Object update(@RequestParam Long id, @AuthAccount Account account, @Validated InternshipOfferForm form,
                               BindingResult bind) {
        SimpleResponse response = new SimpleResponse(bind);
        if (response.hasErrors()) return response;

        InternshipOffer offer = internshipOfferRepository.findById(id);
        if (offer == null)
            return new ResponseEntity<>("Cannot update offer, offer doesn't exist.", HttpStatus.BAD_REQUEST);
        if (!offer.getAccount().equals(account) && !authenticationService.isModerator(account))
            return new ResponseEntity<>("You are not authorized to edit other users offers.", HttpStatus.FORBIDDEN);

        offer.setContent(form.getContent());
        offer.setTitle(form.getTitle());
        offer.setExpirationDate(CVUtil.toTimestamp(form.getExpirationTime()));
        internshipOfferRepository.update(offer);
        return response;
    }

    private boolean isPublished(InternshipOffer offer) {
        return InternshipOfferState.PUBLISHED.equalsIgnoreCase(offer.getInternshipOfferState().getStateName());
    }

    private int calcFrom(int pageNumber) {
        return (pageNumber - 1) * Constants.RESULTS_PER_PAGE;
    }

    private int calcTo(int pageNumber) {
        return pageNumber * Constants.RESULTS_PER_PAGE;
    }
}