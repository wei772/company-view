package ee.idu.vc.repository;

import ee.idu.vc.model.AccountType;
import ee.idu.vc.repository.util.HbnClassificationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HbnAccountTypeRepository extends HbnClassificationRepository<AccountType> implements AccountTypeRepository {
    public HbnAccountTypeRepository() {
        super(AccountType.class, "typeName");
    }
}