package ee.idu.vc.repository;

import ee.idu.vc.model.InternshipOffer;
import ee.idu.vc.repository.util.HbnCRUDRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HbnInternshipOfferRepository extends HbnCRUDRepository<InternshipOffer> implements InternshipOfferRepository {
    public HbnInternshipOfferRepository() {
        super(InternshipOffer.class);
    }
}