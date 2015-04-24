package ee.idu.vc.repository;

import ee.idu.vc.model.Account;
import ee.idu.vc.model.InternshipOffer;
import ee.idu.vc.model.InternshipOfferState;
import ee.idu.vc.util.CVUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

import static org.hibernate.criterion.Restrictions.disjunction;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ilike;

@Repository
public class HbnInternshipOfferRepository implements InternshipOfferRepository {
    private static final int PUBLISHED_STATE_ID = 2;

    @Qualifier("mainSessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public InternshipOffer findById(Long id) {
        if (id == null) throw new IllegalArgumentException("Argument id cannot be null.");
        return CVUtil.tolerantCast(InternshipOffer.class, currentSession().get(InternshipOffer.class, id));
    }

    @Override
    public void create(InternshipOffer internshipOffer) {
        if (internshipOffer == null) throw new IllegalArgumentException("Argument internshipOffer cannot be null.");
        internshipOffer.setInternshipOfferId(null);
        currentSession().save(internshipOffer);
    }

    @Override
    public void update(InternshipOffer internshipOffer) {
        if (internshipOffer == null) throw new IllegalArgumentException("Argument internshipOffer cannot be null.");
        currentSession().update(internshipOffer);
    }

    @Override
    public void delete(InternshipOffer repositoryObject) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public List getInternshipOffersByAccount(Account account, int from, int to) {
        Criteria criteria = currentSession().createCriteria(InternshipOffer.class);
        criteria.add(eq("account", account));
        criteria.setFirstResult(from);
        criteria.setMaxResults(to - from);
        return criteria.list();
    }

    @Override
    public int getInternshipOffersCountByAccount(Account account) {
        Criteria criteria =  currentSession().createCriteria(InternshipOffer.class);
        criteria.add(eq("account", account));
        criteria.setProjection(Projections.rowCount());
        Number rowsCount = ((Number) criteria.uniqueResult());
        return rowsCount.intValue();
    }

    @Override
    public List getPublishedInternshipOffers(int from, int to) {
        InternshipOfferState publishedState = getPublishedState(InternshipOfferState.PUBLISHED);
        Criteria criteria = currentSession().createCriteria(InternshipOffer.class);
        criteria.add(eq("internshipOfferState", publishedState));
        criteria.setFirstResult(from);
        criteria.setMaxResults(to - from);
        return criteria.list();
    }

    @Override
    public List searchInternshipOffers(String keyword, boolean onlyMyInternships, int from, int to, Account account) {
        String state = onlyMyInternships ? InternshipOfferState.UNPUBLISHED : InternshipOfferState.PUBLISHED;
        InternshipOfferState offerState = getPublishedState(state);
        Criteria criteria = currentSession().createCriteria(InternshipOffer.class);
        criteria.add(disjunction()
            .add(ilike("title", keyword))
            .add(ilike("content", keyword))
        );

        if (onlyMyInternships)
            criteria.add(eq("account", account));
        else
            criteria.add(eq("internshipOfferState", offerState));

        criteria.setFirstResult(from);
        criteria.setMaxResults(to - from);
        return criteria.list();
    }

    @Override
    public int getPublishedInternshipOffersCount() {
        InternshipOfferState publishedState = getPublishedState(InternshipOfferState.PUBLISHED);
        Criteria criteria = currentSession().createCriteria(InternshipOffer.class);
        criteria.add(eq("internshipOfferState", publishedState));
        criteria.setProjection(Projections.rowCount());
        Number rowsCount = ((Number) criteria.uniqueResult());
        return rowsCount.intValue();
    }

    private InternshipOfferState getPublishedState(String state) {
        Criteria stateCriteria = currentSession().createCriteria(InternshipOfferState.class);
        stateCriteria.add(eq("stateName", state));
        return (InternshipOfferState) stateCriteria.uniqueResult();
    }


}