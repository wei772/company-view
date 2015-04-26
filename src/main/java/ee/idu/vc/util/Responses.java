package ee.idu.vc.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Responses {
    public static ResponseEntity notFoundInternship(Long internshipOfferId) {
        return notFound("Internship with id " + internshipOfferId + " was not found (it doesn't exist.");
    }

    public static ResponseEntity forbiddenToViewUnpublishedInternship(Long internshipOfferId) {
        return forbidden("Internship with id " + internshipOfferId + " is unpublished. It is forbidden to view " +
                "unpublished offers.");
    }

    public static ResponseEntity forbiddenToSearchAlienUnpublished() {
        return forbidden("It is forbidden to search other users unpublished offers.");
    }

    public static ResponseEntity forbiddenToEditAlienOffer() {
        return forbidden("You are not authorized to edit other users offers.");
    }

    public static ResponseEntity alreadyAppliedToInternship(Long internshipOfferId, Long applicantId) {
        return conflict("Applicant with id " + applicantId + " has already applied to internship offer " + "with id " +
                internshipOfferId + ".");
    }

    public static ResponseEntity accountNotExisting(Long accountId) {
        return notFound("Account with id " + accountId + " doesn't exist.");
    }

    public static ResponseEntity forbiddenToViewAlienUnpublished() {
        return forbidden("It is forbidden to view other users unpublished offers.");
    }

    public static ResponseEntity forbidden(String message) {
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity notFound(String message) {
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity unauthorized(String message) {
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    public static void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.sendError(401, message);
    }

    public static ResponseEntity conflict(String message) {
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    private Responses() {}
}