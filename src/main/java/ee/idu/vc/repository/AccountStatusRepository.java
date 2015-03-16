package ee.idu.vc.repository;

import ee.idu.vc.model.AccountStatus;
import org.springframework.transaction.annotation.Transactional;

public interface AccountStatusRepository {
    @Transactional
    public AccountStatus findById(Long statusId);

    @Transactional
    public AccountStatus findByName(String statusName);
}