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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;

@RestController
public class InternshipOfferController {
    @Autowired
    public InternshipOfferRepository internshipOfferRepository;

    @Autowired
    InternshipOfferStateRepository internshipOfferStateRepository;

    @RequestMapping(value = {"/offer/internship", "/offer/internship/new"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getAngularView() { return new ModelAndView("angular"); }

    @RequireAuth
    @RequestMapping(value = "/offer/internship", method = RequestMethod.POST)
    @ResponseBody
    public Object addInternshipOffer(@Validated InternshipOfferForm form, BindingResult bindResult, @AuthAccount Account account) {
        SimpleResponse response = new SimpleResponse(bindResult);
        Date expirationTime = validatedExpirationTime(form.getExpirationTime(), response);
        if (response.hasErrors()) return response;
        addInternshipToDB(form.getTitle(), form.getContent(), expirationTime, form.isPublish());
        return response;
    }

    private void addInternshipToDB(String title, String content, Date expirationTime, boolean publish) {
        String offerState = publish ? InternshipOfferState.PUBLISHED : InternshipOfferState.UNPUBLISHED;

        InternshipOffer offer = new InternshipOffer();
        offer.setTitle(title);
        offer.setContent(content);
        offer.setExpirationDate(CVUtil.dateToTimestamp(expirationTime));
        offer.setInternshipOfferState(internshipOfferStateRepository.findByName(offerState));
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