package ee.idu.vc.repository.util;

import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public interface CRUDRepository<T extends Serializable> {
    /**
     * Finds the object by the given id.
     * @param id Id.
     * @return Null if id is null or object was not found.
     */
    @Transactional
    T findById(Long id);

    /**
     * Saves the entity. Does not check if the entity already exists.
     * @param repositoryObject The object to save into the repository.
     */
    @Transactional
    void save(T repositoryObject);

    /**
     * Updates the entity. Does not check if entity doesn't exist.
     * @param repositoryObject The object to update in the repository.
     */
    @Transactional
    void update(T repositoryObject);

    /**
     * Deletes the entity in the repository.
     * @param repositoryObject The object to delete in the repository.
     */
    @Transactional
    void delete(T repositoryObject);

    /**
     * Deletes the entity in the repository.
     * @param id The id of the object to delete in the repository.
     */
    @Transactional
    void delete(Long id);
}