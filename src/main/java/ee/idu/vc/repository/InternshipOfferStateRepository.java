package ee.idu.vc.repository;

import ee.idu.vc.model.InternshipOfferState;

import javax.transaction.Transactional;

public interface InternshipOfferStateRepository {
    @Transactional
    public InternshipOfferState findById(Long id);

    @Transactional
    public InternshipOfferState findByName(String stateName);
}