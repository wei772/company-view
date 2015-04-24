package ee.idu.vc.repository.util;

import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public interface ClassificationRepository<T extends Serializable> {
    /**
     * Fins the classification object by the id.
     * @param id Classification id.
     * @return Classification object or null if not found.
     */
    @Transactional
    T findById(Long id);

    /**
     * Finds the classification object by the name.
     * @param name Classification name.
     * @return Classification object or null if not found.
     */
    @Transactional
    T findByName(String name);
}