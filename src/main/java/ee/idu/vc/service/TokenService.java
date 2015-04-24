package ee.idu.vc.service;

import ee.idu.vc.model.Account;
import ee.idu.vc.model.Token;

import javax.transaction.Transactional;

public interface TokenService {
    @Transactional
    Token latestToken(Account account);

    @Transactional
    Token newToken(Account account);

    @Transactional
    void extendToken(Token token);

    @Transactional
    Token latestValidToken(Account account);
}