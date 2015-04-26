package ee.idu.vc.controller;

import ee.idu.vc.controller.response.InternshipsSearchResponse;
import ee.idu.vc.controller.response.NewItemResponse;
import ee.idu.vc.model.Account;
import ee.idu.vc.model.InternshipApplicant;
import ee.idu.vc.model.InternshipOffer;
import ee.idu.vc.model.InternshipOfferState;
import ee.idu.vc.repository.AccountRepository;
import ee.idu.vc.repository.InternshipApplicantRepository;
import ee.idu.vc.repository.InternshipOfferRepository;
import ee.idu.vc.service.InternshipApplicantService;
import ee.idu.vc.service.InternshipService;
import ee.idu.vc.util.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        if (offer == null) return Responses.internshipNotExisting(id);
        if (isPublished(offer)) return offer;
        return Responses.internshipUnpublished(id);
    }

    @RequestMapping(value = "/api/internship", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object applyForInternship(@RequestParam(required = true) Long id, @RequestParam(required = true) Long studentId){
        InternshipOffer offer = internshipOfferRepository.findById(id);
        if (offer == null) return Responses.internshipNotExisting(id);
        if (!isPublished(offer)) return Responses.internshipUnpublished(id);

        InternshipApplicant existingApplicant = internshipApplicantService.getInternshipApplicant(studentId, id);
        if (existingApplicant != null) return Responses.alreadyAppliedToInternship(id, studentId);

        InternshipApplicant applicant = new InternshipApplicant();
        applicant.setInternshipOffer(offer);
        applicant.setStudentId(studentId);
        internshipApplicantRepository.save(applicant);

        return new NewItemResponse(applicant.getInternshipApplicantId());
    }

    @RequestMapping(value = "/api/account", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getAccountById(@RequestParam(required = true) Long id) {
        Account account =  accountRepository.findById(id);
        return account != null ? account : Responses.accountNotExisting(id);
    }
}
