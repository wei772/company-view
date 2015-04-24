package ee.idu.vc.repository.util;

import ee.idu.vc.util.CVUtil;
import ee.idu.vc.util.HbnSessionProvider;

import java.io.Serializable;

public abstract class HbnCRUDRepository<T extends Serializable> extends HbnSessionProvider implements CRUDRepository<T> {
    private final Class<T> entityType;

    public HbnCRUDRepository(Class<T> entityType) {
        if (entityType == null) throw new IllegalArgumentException("Argument entityType cannot be null.");
        this.entityType = entityType;
    }

    @Override
    public T findById(Long id) {
        if (id == null) return null;
        return CVUtil.tolerantCast(entityType, currentSession().get(entityType, id));
    }

    @Override
    public void save(T repositoryObject) {
        if (repositoryObject == null) throw new IllegalArgumentException("Argument repositoryObject cannot be null.");
        currentSession().save(repositoryObject);
    }

    @Override
    public void update(T repositoryObject) {
        if (repositoryObject == null) throw new IllegalArgumentException("Argument repositoryObject cannot be null.");
        currentSession().update(repositoryObject);
    }

    @Override
    public void delete(T repositoryObject) {
        if (repositoryObject == null) throw new IllegalArgumentException("Argument repositoryObject cannot be null.");
        currentSession().delete(repositoryObject);
    }

    @Override
    public void delete(Long id) {
        if (id == null) throw new IllegalArgumentException("Argument id cannot be null.");
        T object = findById(id);
        if (object != null) currentSession().delete(object);
    }
}