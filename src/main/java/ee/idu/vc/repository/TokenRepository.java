package ee.idu.vc.repository;

import ee.idu.vc.model.Token;

public interface TokenRepository {
    public Token getMostRecent(Long userId);
    public Token createFreshToken(Long userId);
}