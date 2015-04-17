package ee.idu.vc.repository;

import ee.idu.vc.model.Account;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends GeneralRepository<Account> {
    @Transactional
    public Account findByEmailIgnoreCase(String email);

    @Transactional
    public Account findByUsernameIgnoreCase(String username);

    @Transactional
    public Account findByUsername(String username);
}