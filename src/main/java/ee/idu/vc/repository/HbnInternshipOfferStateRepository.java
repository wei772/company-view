package ee.idu.vc.repository;

import ee.idu.vc.model.InternshipOfferState;
import ee.idu.vc.repository.util.HbnClassificationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HbnInternshipOfferStateRepository extends HbnClassificationRepository<InternshipOfferState> implements InternshipOfferStateRepository {
    public HbnInternshipOfferStateRepository() {
        super(InternshipOfferState.class, "stateName");
    }
}