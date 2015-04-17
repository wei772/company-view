package ee.idu.vc.repository;

import ee.idu.vc.model.Token;
import org.springframework.transaction.annotation.Transactional;

public interface TokenRepository {
    @Transactional
    public Token getMostRecent(Long accountId);

    @Transactional
    public Token createFreshToken(Long accountId);

    @Transactional
    public void updateToken(Token token);
}