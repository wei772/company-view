package ee.idu.vc.controller;

import ee.idu.vc.auth.AuthAccount;
import ee.idu.vc.auth.RequireAuth;
import ee.idu.vc.controller.form.TraineeshipForm;
import ee.idu.vc.controller.response.SimpleResponse;
import ee.idu.vc.model.Account;
import ee.idu.vc.model.Traineeship;
import ee.idu.vc.model.TraineeshipState;
import ee.idu.vc.repository.TraineeshipRepository;
import ee.idu.vc.repository.TraineeshipStateRepository;
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
public class TraineeshipOfferController {
    @Autowired
    public TraineeshipRepository traineeshipRepository;

    @Autowired
    TraineeshipStateRepository traineeshipStateRepository;

    @RequestMapping(value = {"/offer/traineeship", "/offer/traineeship/new"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getAngularView() { return new ModelAndView("angular"); }

    @RequireAuth
    @RequestMapping(value = "/offer/traineeship", method = RequestMethod.POST)
    @ResponseBody
    public Object addNewTraineeship(@Validated TraineeshipForm form, BindingResult bindResult, @AuthAccount Account account) {
        SimpleResponse response = new SimpleResponse(bindResult);
        Date expirationTime = validatedExpirationTime(form.getExpirationTime(), response);
        if (response.hasErrors()) return response;
        addTraineeshipOfferToDB(form.getTitle(), form.getContent(), expirationTime, form.isPublish());
        return response;
    }

    private void addTraineeshipOfferToDB(String title, String content, Date expirationTime, boolean publish) {
        TraineeshipState publishedState = traineeshipStateRepository.findByName(TraineeshipState.PUBLISHED);
        TraineeshipState unpublishedState = traineeshipStateRepository.findByName(TraineeshipState.UNPUBLISHED);

        Traineeship traineeship = new Traineeship();
        traineeship.setTitle(title);
        traineeship.setContent(content);
        traineeship.setExpirationDate(CVUtil.dateToTimestamp(expirationTime));
        traineeship.setTraineeshipState(publish ? publishedState : unpublishedState);
        traineeshipRepository.create(traineeship);
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