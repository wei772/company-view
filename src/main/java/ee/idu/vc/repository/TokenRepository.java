package ee.idu.vc.repository;

import ee.idu.vc.model.Token;
import org.springframework.transaction.annotation.Transactional;

public interface TokenRepository {
    @Transactional("localDBTransactionManager")
    public Token getMostRecent(Long userId);

    @Transactional("localDBTransactionManager")
    public Token createFreshToken(Long userId);

    @Transactional("localDBTransactionManager")
    public void updateToken(Token token);
}