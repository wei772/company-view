package ee.idu.vc.service;

import ee.idu.vc.model.Account;

import javax.transaction.Transactional;

public interface AuthenticationService {
    @Transactional
    Account loginWithCredentials(String username, String password);

    @Transactional
    Account loginWithToken(String username, String tokenUUID);

    @Transactional
    boolean isValidPassword(Account passwordOwner, String passwordToCheck);

    @Transactional
    boolean isBanned(Account account);
}