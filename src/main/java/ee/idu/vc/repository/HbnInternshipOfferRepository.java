package ee.idu.vc.repository;

import ee.idu.vc.model.Account;
import ee.idu.vc.model.InternshipOffer;
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

import static org.hibernate.criterion.Restrictions.eq;

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
        Criteria criteria = currentSession().createCriteria(InternshipOffer.class);
        criteria.add(eq("internshipOfferStateId", PUBLISHED_STATE_ID));
        criteria.setFirstResult(from);
        criteria.setMaxResults(to - from);
        return criteria.list();
    }

    @Override
    public int getPublishedInternshipOffersCount() {
        Criteria criteria = currentSession().createCriteria(InternshipOffer.class);
        criteria.add(eq("internshipOfferStateId", PUBLISHED_STATE_ID));
        criteria.setProjection(Projections.rowCount());
        Number rowsCount = ((Number) criteria.uniqueResult());
        return rowsCount.intValue();
    }
}