package ee.idu.vc.controller;

import ee.idu.vc.auth.AuthAccount;
import ee.idu.vc.auth.RequireAuth;
import ee.idu.vc.controller.form.InternshipOfferForm;
import ee.idu.vc.controller.response.JsonResponse;
import ee.idu.vc.controller.response.NewItemResponse;
import ee.idu.vc.controller.response.SimpleResponse;
import ee.idu.vc.model.Account;
import ee.idu.vc.model.InternshipOffer;
import ee.idu.vc.repository.AccountRepository;
import ee.idu.vc.repository.InternshipOfferRepository;
import ee.idu.vc.service.AuthenticationService;
import ee.idu.vc.service.InternshipService;
import ee.idu.vc.util.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static ee.idu.vc.util.CVUtil.*;

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
        Account account = isStringEmpty(username) ? null : accountRepository.findByUsername(username, false);
        if (!requester.equals(account) && !onlyPublished && !authenticationService.isModerator(requester))
            return Responses.notAuthorizedToSearchUnpublished();

        if (page == null || page < 1) page = 1;
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
        if (offer == null) return Responses.internshipNotExisting(id);
        if (isPublished(offer)) return offer;
        return account.equals(offer.getAccount()) ? offer : Responses.notAuthorizedToViewUnpublished();
    }

    @RequireAuth
    @RequestMapping(value = "/offer/internship", method = RequestMethod.PUT)
    @ResponseBody
    public Object update(@RequestParam Long id, @AuthAccount Account account, @Validated InternshipOfferForm form,
                               BindingResult bind) {
        SimpleResponse response = new SimpleResponse(bind);
        if (response.hasErrors()) return response;

        InternshipOffer offer = internshipOfferRepository.findById(id);
        if (offer == null) return Responses.internshipNotExisting(id);

        if (!offer.getAccount().equals(account) && !authenticationService.isModerator(account))
            return Responses.notAuthorizedToEditOffer();

        offer.setContent(form.getContent());
        offer.setTitle(form.getTitle());
        offer.setExpirationDate(toTimestamp(form.getExpirationTime()));
        internshipOfferRepository.update(offer);
        return response;
    }
}