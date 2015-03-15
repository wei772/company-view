package ee.idu.vc.repository;

import ee.idu.vc.model.User;

public interface UserRepository extends GeneralRepository<User> {
    public User findByCredentials(String username, String password);
}