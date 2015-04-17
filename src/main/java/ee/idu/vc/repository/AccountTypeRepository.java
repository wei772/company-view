package ee.idu.vc.repository;

import ee.idu.vc.model.AccountType;
import org.springframework.transaction.annotation.Transactional;

public interface AccountTypeRepository {
    @Transactional
    public AccountType findById(Long id);

    @Transactional
    public AccountType findByName(String typeName);
}
