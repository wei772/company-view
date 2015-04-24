package ee.idu.vc.repository.util;

import ee.idu.vc.util.CVUtil;
import ee.idu.vc.util.HbnSessionProvider;
import org.hibernate.Criteria;

import java.io.Serializable;

import static org.hibernate.criterion.Restrictions.eq;

public class HbnClassificationRepository<T extends Serializable> extends HbnSessionProvider implements ClassificationRepository<T> {
    private final Class<T> entityType;
    private final String nameField;

    public HbnClassificationRepository(Class<T> entityType, String nameField) {
        if (entityType == null) throw new IllegalArgumentException("Argument entityType cannot be null.");
        if (nameField == null) throw new IllegalArgumentException("Argument nameField cannot be null.");
        this.entityType = entityType;
        this.nameField = nameField;
    }

    @Override
    public T findById(Long id) {
        if (id == null) throw new IllegalArgumentException("Argument id cannot be null.");
        return entityType.cast(currentSession().get(entityType, id));
    }

    @Override
    public T findByName(String name) {
        if (name == null) throw new IllegalArgumentException("Argument name cannot be null.");
        Criteria criteria = currentSession().createCriteria(entityType);
        criteria.add(eq(nameField, name));
        return CVUtil.tolerantCast(entityType, criteria.uniqueResult());
    }
}