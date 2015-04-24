package ee.idu.vc.controller;

import ee.idu.vc.auth.AuthAccount;
import ee.idu.vc.auth.RequireAuth;
import ee.idu.vc.controller.form.InternshipOfferForm;
import ee.idu.vc.controller.response.SimpleResponse;
import ee.idu.vc.model.Account;
import ee.idu.vc.model.InternshipOffer;
import ee.idu.vc.model.InternshipOfferState;
import ee.idu.vc.repository.InternshipOfferRepository;
import ee.idu.vc.repository.InternshipOfferStateRepository;
import ee.idu.vc.util.CVUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class InternshipOfferController {
    @Autowired
    public InternshipOfferRepository internshipOfferRepository;

    @Autowired
    InternshipOfferStateRepository internshipOfferStateRepository;

    @RequestMapping(value = {"/offer/internships", "/offer/internships/new", "/offer/internships/all"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getAngularView() { return new ModelAndView("angular"); }

    @RequireAuth
    @RequestMapping(value = "/offer/myinternships", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<InternshipOffer> getMyInternships(@RequestParam(required = false, defaultValue = "1") String page, @AuthAccount Account account) {
        Integer pageNumber = CVUtil.parseInt(page);
        if (pageNumber == null || pageNumber < 1) pageNumber = 1;
        int from = (pageNumber - 1) * 20;
        int to = pageNumber * 20;
        return internshipOfferRepository.getInternshipOffersByAccount(account, from, to);
    }

    @RequireAuth
    @RequestMapping(value = "/offer/myinternships/count", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public int getMyInternshipsCount(@AuthAccount Account account) {
        return internshipOfferRepository.getInternshipOffersCountByAccount(account);
    }

    @RequireAuth
    @RequestMapping(value = "/offer/allinternships", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<InternshipOffer> getAllInternships(@RequestParam(required = false, defaultValue = "1") String page) {
        Integer pageNumber = CVUtil.parseInt(page);
        if (pageNumber == null || pageNumber < 1) pageNumber = 1;
        int from = (pageNumber - 1) * 2;
        int to = pageNumber * 2;
        return internshipOfferRepository.getPublishedInternshipOffers(from, to);
    }

    @RequireAuth
    @RequestMapping(value = "/offer/allinternships/count", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public int getAllInternshipsCount() {
        return internshipOfferRepository.getPublishedInternshipOffersCount();
    }

    @RequireAuth
    @RequestMapping(value = "/offer/internship", method = RequestMethod.POST)
    @ResponseBody
    public Object addInternshipOffer(@Validated InternshipOfferForm form, BindingResult bindResult, @AuthAccount Account account) {
        SimpleResponse response = new SimpleResponse(bindResult);
        Date expirationTime = validatedExpirationTime(form.getExpirationTime(), response);
        if (response.hasErrors()) return response;
        addInternshipToDB(form.getTitle(), form.getContent(), expirationTime, form.isPublish(), account);
        return response;
    }

    private void addInternshipToDB(String title, String content, Date expirationTime, String publish, Account account) {
        InternshipOffer offer = new InternshipOffer();
        offer.setTitle(title);
        offer.setContent(content);
        offer.setExpirationDate(CVUtil.dateToTimestamp(expirationTime));
        offer.setInternshipOfferState(internshipOfferStateRepository.findByName(publish));
        offer.setAccount(account);
        internshipOfferRepository.create(offer);
    }

    private Date validatedExpirationTime(String expirationTimeString, SimpleResponse response) {
        Date expirationTime = CVUtil.parseDateTime(expirationTimeString);
        if (expirationTime == null) {
            if (expirationTimeString != null && !expirationTimeString.isEmpty())
                response.addError("expirationTime", "Invalid expiration date format.");
            return null;
        }

        Date currentTime = Calendar.getInstance().getTime();
        if (expirationTime.getTime() < currentTime.getTime() + 24 * 3600 * 1000) {
            response.addError("expirationTime", "Expiration time must be at least 24h in the future.");
            return null;
        }

        return expirationTime;
    }
}