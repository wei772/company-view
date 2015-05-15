package ee.idu.vc.controller;

import ee.idu.vc.controller.response.InternshipsSearchResponse;
import ee.idu.vc.controller.response.ItemIdResponse;
import ee.idu.vc.model.Account;
import ee.idu.vc.model.InternshipApplicant;
import ee.idu.vc.model.InternshipOffer;
import ee.idu.vc.repository.AccountRepository;
import ee.idu.vc.repository.InternshipApplicantRepository;
import ee.idu.vc.repository.InternshipOfferRepository;
import ee.idu.vc.service.InternshipApplicantService;
import ee.idu.vc.service.InternshipService;
import ee.idu.vc.util.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ee.idu.vc.util.CVUtil.calcFrom;
import static ee.idu.vc.util.CVUtil.calcTo;
import static ee.idu.vc.util.CVUtil.isPublished;

@RestController
public class ApiController {
    @Autowired
    private InternshipService internshipService;

    @Autowired
    private InternshipApplicantService internshipApplicantService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InternshipOfferRepository internshipOfferRepository;

    @Autowired
    private InternshipApplicantRepository internshipApplicantRepository;

    @RequestMapping(value = "/api/internships", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public InternshipsSearchResponse searchInternships(@RequestParam(required = false) Integer page,
                                                       @RequestParam(required = false) String keyword) {
        if (page == null || page < 1) page = 1;
        List<InternshipOffer> offers = internshipService.searchInternships(calcFrom(page), calcTo(page), true, null, keyword);
        int count = internshipService.internshipSearchResultsCount(true, null, keyword);
        return new InternshipsSearchResponse(offers, count);
    }

    @RequestMapping(value = "/api/internship", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getInternshipById(@RequestParam(required = true) Long id) {
        InternshipOffer offer = internshipOfferRepository.findById(id);
        if (offer == null) return Responses.notFoundInternship(id);
        if (isPublished(offer)) return offer;
        return Responses.forbiddenToViewUnpublishedInternship(id);
    }

    @RequestMapping(value = "/api/internship", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object applyForInternship(@RequestParam(required = true) Long internshipOfferId, @RequestParam(required = true) Long studentId){
        InternshipOffer offer = internshipOfferRepository.findById(internshipOfferId);
        if (offer == null) return Responses.notFoundInternship(internshipOfferId);
        if (!isPublished(offer)) return Responses.forbiddenToViewUnpublishedInternship(internshipOfferId);

        InternshipApplicant existingApplicant = internshipApplicantService.getInternshipApplicant(studentId, internshipOfferId);
        if (existingApplicant != null) return Responses.alreadyAppliedToInternship(internshipOfferId, studentId);

        InternshipApplicant applicant = new InternshipApplicant();
        applicant.setInternshipOffer(offer);
        applicant.setStudentId(studentId);
        internshipApplicantRepository.save(applicant);

        return new ItemIdResponse(applicant.getInternshipApplicantId());
    }

    @RequestMapping(value = "/api/internship", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteInternshipApplication(@RequestParam(required = true) Long internshipOfferId, @RequestParam(required = true) Long studentId){
        InternshipApplicant applicant = internshipApplicantService.getInternshipApplicant(studentId, internshipOfferId);
        if (applicant == null) return Responses.notFound("Cannot delete a non-existing application.");
        internshipApplicantRepository.delete(applicant);
        return new ItemIdResponse(applicant.getInternshipApplicantId());
    }

    @RequestMapping(value = "/api/account", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getAccountById(@RequestParam(required = true) Long id) {
        Account account =  accountRepository.findById(id);
        return account != null ? account : Responses.accountNotExisting(id);
    }
}
