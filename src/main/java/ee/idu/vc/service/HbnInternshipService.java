package ee.idu.vc.service;

import ee.idu.vc.controller.form.InternshipOfferForm;
import ee.idu.vc.model.Account;
import ee.idu.vc.model.InternshipOffer;
import ee.idu.vc.model.InternshipOfferState;
import ee.idu.vc.repository.InternshipOfferRepository;
import ee.idu.vc.repository.InternshipOfferStateRepository;
import ee.idu.vc.util.CVUtil;
import ee.idu.vc.util.HbnSessionProvider;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.hibernate.criterion.Restrictions.disjunction;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ilike;

@Service
public class HbnInternshipService extends HbnSessionProvider implements InternshipService {
    @Autowired
    private InternshipOfferStateRepository stateRepository;

    @Autowired
    private InternshipOfferRepository offerRepository;

    @Override
    @SuppressWarnings("unchecked")
    public List<InternshipOffer> searchInternships(int from, int to, boolean onlyPublished, Account account, String keyword) {
        Criteria criteria = internshipSearchCriteria(onlyPublished, account, keyword);
        criteria.setFirstResult(from);
        criteria.setMaxResults(to - from);
        return criteria.list();
    }

    @Override
    public List<InternshipOffer> searchInternships(int from, int to, boolean onlyPublished, Account account) {
        return this.searchInternships(from, to, onlyPublished, account, null);
    }

    @Override
    public List<InternshipOffer> searchInternships(int from, int to, boolean onlyPublished) {
        return this.searchInternships(from, to, onlyPublished, null, null);
    }

    @Override
    public int internshipSearchResultsCount(boolean onlyPublished) {
        return this.internshipSearchResultsCount(onlyPublished, null, null);
    }

    @Override
    public int internshipSearchResultsCount(boolean onlyPublished, Account account) {
        return this.internshipSearchResultsCount(onlyPublished, account, null);
    }

    @Override
    public int internshipSearchResultsCount(boolean onlyPublished, Account account, String keyword) {
        Criteria criteria = internshipSearchCriteria(onlyPublished, account, keyword);
        criteria.setProjection(Projections.rowCount());
        Number rowsCount = ((Number) criteria.uniqueResult());
        return rowsCount.intValue();
    }

    @Override
    public InternshipOffer createAndSave(InternshipOfferForm form, Account creator) {
        InternshipOffer offer = new InternshipOffer();
        offer.setTitle(form.getTitle());
        offer.setContent(form.getContent());
        offer.setExpirationDate(CVUtil.toTimestamp(form.getExpirationTime()));
        offer.setInternshipOfferState(getPublishAsState(form.publish()));
        offer.setAccount(creator);
        offerRepository.save(offer);
        return offer;
    }

    private InternshipOfferState getPublishAsState(boolean publish) {
        String stateName = publish ? InternshipOfferState.PUBLISHED : InternshipOfferState.UNPUBLISHED;
        return stateRepository.findByName(stateName);
    }

    private Criteria internshipSearchCriteria(boolean onlyPublished, Account account, String keyword) {
        Criteria criteria = currentSession().createCriteria(InternshipOffer.class);
        if (onlyPublished) criteria.add(eq("internshipOfferState", stateRepository.findByName("published")));
        if (account != null) criteria.add(eq("account", account));
        if (keyword != null) criteria.add(disjunction().add(ilike("title", keyword)).add(ilike("content", keyword)));
        return criteria;
    }
}