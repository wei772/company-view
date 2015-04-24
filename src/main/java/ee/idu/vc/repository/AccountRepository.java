package ee.idu.vc.repository;

import ee.idu.vc.model.Account;
import ee.idu.vc.repository.util.CRUDRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends CRUDRepository<Account> {
    /**
     * Finds the account by the e-mail address.
     * @param email E-mail address of the account.
     * @param caseSensitive Set true to make the search case sensitive.
     * @return Account or null if not found (or if email is null).
     */
    @Transactional
    Account findByEmail(String email, boolean caseSensitive);

    /**
     * Finds the account by the username.
     * @param username Username.
     * @param caseSensitive Set true to make the search case sensitive.
     * @return Account or null if not found (or if username is null).
     */
    @Transactional
    Account findByUsername(String username, boolean caseSensitive);
}