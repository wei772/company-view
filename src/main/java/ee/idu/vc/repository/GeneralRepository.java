package ee.idu.vc.repository;

import java.io.Serializable;

public interface GeneralRepository<T extends Serializable> {
    public T findById(Long id);
    public void create(T repositoryObject);
    public void update(T repositoryObject);
    public void delete(T repositoryObject);
    public void delete(Long id);
}