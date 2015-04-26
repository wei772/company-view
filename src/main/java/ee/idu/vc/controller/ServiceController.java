package ee.idu.vc.controller;

import ee.idu.vc.controller.response.NewItemResponse;
import ee.idu.vc.model.InternshipApplicant;
import ee.idu.vc.model.InternshipOffer;
import ee.idu.vc.repository.InternshipApplicantRepository;
import ee.idu.vc.repository.InternshipOfferRepository;
import ee.idu.vc.service.InternshipService;
import ee.idu.vc.util.CVUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServiceController {
    @Autowired
    private InternshipService internshipService;

    @Autowired
    private InternshipOfferRepository internshipOfferRepository;

    @Autowired
    private InternshipApplicantRepository internshipApplicantRepository;

    @RequestMapping(value = "/api/internships", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object search(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String keyword) {
        if (page == null || page < 1) page = 1;
        return internshipService.searchInternships(CVUtil.calcFrom(page), CVUtil.calcTo(page), true, null, keyword);
    }

    @RequestMapping(value = "/api/internship", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object search(
            @RequestParam(required = true) Long id){
        InternshipOffer offer = internshipOfferRepository.findById(id);
        if (offer == null) return new ResponseEntity<>("Internship with id "+id+" doesn't exist.", HttpStatus.NOT_FOUND);
        if (CVUtil.isPublished(offer)) return offer;
        return new ResponseEntity<>("Internship with id "+id+" is unpublished.", HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/api/internship", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object search(@RequestParam(required = true) Long id, @RequestParam(required = true) Long studentId){
        InternshipOffer offer = internshipOfferRepository.findById(id);
        if (offer == null) return new ResponseEntity<>("Internship with id "+id+" doesn't exist.", HttpStatus.NOT_FOUND);
        if (!CVUtil.isPublished(offer)) return new ResponseEntity<>("Internship with id "+id+" is unpublished.", HttpStatus.UNAUTHORIZED);
        InternshipApplicant applicant = new InternshipApplicant();
        applicant.setInternshipOffer(offer);
        applicant.setStudentId(studentId);
        internshipApplicantRepository.save(applicant);
        return new NewItemResponse(applicant.getInternshipApplicantId());
    }
}
