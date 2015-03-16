package ee.idu.vc.repository;

import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public interface GeneralRepository<T extends Serializable> {
    @Transactional
    public T findById(Long id);

    @Transactional
    public void create(T repositoryObject);

    @Transactional
    public void update(T repositoryObject);

    @Transactional
    public void delete(T repositoryObject);

    @Transactional
    public void delete(Long id);
}