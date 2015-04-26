package ee.idu.vc.repository;

import ee.idu.vc.model.InternshipApplicant;
import ee.idu.vc.repository.util.HbnCRUDRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HbnInternshipApplicantRepository extends HbnCRUDRepository<InternshipApplicant> implements InternshipApplicantRepository{
    public HbnInternshipApplicantRepository(){
        super(InternshipApplicant.class);
    }
}
