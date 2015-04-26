package ee.idu.vc.service;

import ee.idu.vc.model.InternshipApplicant;
import ee.idu.vc.repository.InternshipOfferRepository;
import ee.idu.vc.util.HbnSessionProvider;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static org.hibernate.criterion.Restrictions.eq;

@Service
public class HbnInternshipApplicantService extends HbnSessionProvider implements InternshipApplicantService{

    @Autowired
    private InternshipOfferRepository offerRepository;

    @Override
    public InternshipApplicant getInternshipApplicant(long studentId, long internshipOfferId) {
        Criteria criteria = currentSession().createCriteria(InternshipApplicant.class);
        criteria.add(eq("studentId", studentId));
        criteria.add(eq("internshipOffer", offerRepository.findById(internshipOfferId)));
        return (InternshipApplicant) criteria.uniqueResult();
    }
}
