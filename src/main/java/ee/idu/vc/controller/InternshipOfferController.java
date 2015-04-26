package ee.idu.vc.controller;

import ee.idu.vc.auth.AuthAccount;
import ee.idu.vc.auth.RequireAuth;
import ee.idu.vc.controller.form.InternshipOfferForm;
import ee.idu.vc.controller.response.InternshipsSearchResponse;
import ee.idu.vc.controller.response.JsonResponse;
import ee.idu.vc.controller.response.NewItemResponse;
import ee.idu.vc.controller.response.SimpleResponse;
import ee.idu.vc.model.Account;
import ee.idu.vc.model.InternshipOffer;
import ee.idu.vc.repository.AccountRepository;
import ee.idu.vc.repository.InternshipOfferRepository;
import ee.idu.vc.service.AuthenticationService;
import ee.idu.vc.service.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static ee.idu.vc.util.CVUtil.*;
import static ee.idu.vc.util.Responses.*;
import static ee.idu.vc.util.Responses.forbiddenToViewAlienUnpublished;

@RestController
public class InternshipOfferController {
    @Autowired
    private InternshipService internshipService;

    @Autowired
    private InternshipOfferRepository internshipOfferRepository;

    @Autowired
    private AuthenticationService authService;

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
                         @RequestParam(required = false) String keyword, @AuthAccount Account searcher) {
        Account accountToSearch = isStringEmpty(username) ? null : accountRepository.findByUsername(username, false);
        if (!authService.hasRightsToSearch(searcher, accountToSearch, onlyPublished)) return forbiddenToSearchAlienUnpublished();
        if (page == null || page < 1) page = 1;
        List<InternshipOffer> offers = internshipService
                .searchInternships(calcFrom(page), calcTo(page), onlyPublished, accountToSearch, keyword);
        int count = internshipService.internshipSearchResultsCount(onlyPublished, accountToSearch, keyword);
        return new InternshipsSearchResponse(offers, count);
    }

    @RequireAuth
    @RequestMapping(value = "/offer/internship", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse postInternship(@Validated InternshipOfferForm form, BindingResult bind, @AuthAccount Account account) {
        SimpleResponse response = new SimpleResponse(bind);
        if (response.hasErrors()) return response;

        Long internshipOfferId = internshipService.createAndSave(form, account).getInternshipOfferId();
        return new NewItemResponse(internshipOfferId);
    }

    @RequireAuth
    @RequestMapping(value = "/offer/internship", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getInternship(@RequestParam Long id, @AuthAccount Account viewer) {
        InternshipOffer offer = internshipOfferRepository.findById(id);
        if (offer == null) return notFoundInternship(id);
        return authService.hasRightsToView(viewer, offer) ? offer : forbiddenToViewAlienUnpublished();
    }

    @RequireAuth
    @RequestMapping(value = "/offer/internship", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateInternship(@RequestParam Long id, @AuthAccount Account editor, @Validated InternshipOfferForm form,
                               BindingResult bind) {
        SimpleResponse response = new SimpleResponse(bind);
        if (response.hasErrors()) return response;

        InternshipOffer offer = internshipOfferRepository.findById(id);
        if (offer == null) return notFoundInternship(id);
        if (!authService.hasRightsToEdit(editor, offer)) return forbiddenToEditAlienOffer();

        offer.setContent(form.getContent());
        offer.setTitle(form.getTitle());
        offer.setExpirationDate(toTimestamp(form.getExpirationTime()));
        internshipOfferRepository.update(offer);
        return response;
    }
}