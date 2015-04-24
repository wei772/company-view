package ee.idu.vc.repository;

import ee.idu.vc.model.Account;
import ee.idu.vc.model.InternshipOffer;

import javax.transaction.Transactional;
import java.util.List;

public interface InternshipOfferRepository extends GeneralRepository<InternshipOffer> {
    @Transactional
    public List getInternshipOffersByAccount(Account account, int from, int to);

    @Transactional
    public List getPublishedInternshipOffers(int from, int to);

    @Transactional
    public List searchInternshipOffers(String keyword, boolean onlyMyInternships, int from, int to, Account account);

    @Transactional
    public int getInternshipOffersCountByAccount(Account account);

    @Transactional
    public int getPublishedInternshipOffersCount();
}