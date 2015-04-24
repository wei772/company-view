package ee.idu.vc.repository;

import ee.idu.vc.model.AccountStatus;
import ee.idu.vc.repository.util.HbnClassificationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HbnAccountStatusRepository extends HbnClassificationRepository<AccountStatus> implements AccountStatusRepository {
    public HbnAccountStatusRepository() {
        super(AccountStatus.class, "statusName");
    }
}