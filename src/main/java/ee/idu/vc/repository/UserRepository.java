package ee.idu.vc.repository;

import ee.idu.vc.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends GeneralRepository<User> {
    @Transactional
    public User findByCredentials(String username, String password);

    @Transactional
    public User findByEmailIgnoreCase(String email);

    @Transactional
    public User findByUsernameIgnoreCase(String username);

    @Transactional
    public User findByUsername(String username);
}