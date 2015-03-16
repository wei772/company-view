package ee.idu.vc.repository;

import ee.idu.vc.model.UserType;
import org.springframework.transaction.annotation.Transactional;

public interface UserTypeRepository {
    @Transactional
    public UserType findById(Long id);

    @Transactional
    public UserType findByName(String typeName);
}
