package ee.idu.vc.service;

import ee.idu.vc.model.InternshipApplicant;

import javax.transaction.Transactional;

public interface InternshipApplicantService {
    @Transactional
    InternshipApplicant getInternshipApplicant(long studentId, long internshipOfferId);
}