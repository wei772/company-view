package ee.idu.vc.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Responses {
    public static ResponseEntity internshipNotExisting(Long internshipOfferId) {
        return new ResponseEntity<>("Internship with id " + internshipOfferId + " doesn't exist.",
                HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity internshipUnpublished(Long internshipOfferId) {
        return new ResponseEntity<>("Internship with id " + internshipOfferId + " is unpublished. You are not " +
                "authorized to view unpublished internship offers.", HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity notAuthorizedToSearchUnpublished() {
        return new ResponseEntity<>("Your account has not enough privileges to search other users unpublished offers.",
                HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity notAuthorizedToEditOffer() {
        return new ResponseEntity<>("You are not authorized to edit other users offers.", HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity alreadyAppliedToInternship(Long internshipOfferId, Long applicantId) {
        return new ResponseEntity<>("Applicant with id " + applicantId + " has already applied to internship offer " +
                "with id " + internshipOfferId + ".", HttpStatus.CONFLICT);
    }

    public static ResponseEntity accountNotExisting(Long accountId) {
        return new ResponseEntity<>("Account with id " + accountId + " doesn't exist.", HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity notAuthorizedToViewUnpublished() {
        return new ResponseEntity<>("It is forbidden to view other users unpublished offers.", HttpStatus.FORBIDDEN);
    }

    private Responses() {}
}