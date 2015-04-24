package ee.idu.vc.repository;

import ee.idu.vc.model.Token;
import ee.idu.vc.repository.util.HbnCRUDRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HbnTokenRepository extends HbnCRUDRepository<Token> implements TokenRepository {
    public HbnTokenRepository() {
        super(Token.class);
    }
}