package ee.idu.vc.service;

import ee.idu.vc.model.*;
import ee.idu.vc.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static ee.idu.vc.util.CVUtil.isPublished;

@Service
public class HbnAuthenticationService implements AuthenticationService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public Account loginWithCredentials(String username, String password) {
        if (username == null) throw new IllegalArgumentException("Argument username cannot be null.");
        if (password == null) throw new IllegalArgumentException("Argument password cannot be null.");

        Account account = accountRepository.findByUsername(username, false);
        if (account == null) return null;

        return isValidPassword(account, password) ? account : null;
    }

    @Override
    public Account loginWithToken(String username, String tokenUUID) {
        if (username == null) throw new IllegalArgumentException("Argument username cannot be null.");
        if (tokenUUID == null) throw new IllegalArgumentException("Argument tokenUUID cannot be null.");

        Account account = accountRepository.findByUsername(username, false);
        if (account == null) return null;

        Token token = tokenService.latestToken(account);
        if (token == null) return null;

        if (!token.getUuid().equals(UUID.fromString(tokenUUID))) return null;
        tokenService.extendToken(token);
        return account;
    }

    @Override
    public boolean isValidPassword(Account passwordOwner, String passwordToCheck) {
        if (passwordOwner == null) throw new IllegalArgumentException("Argument passwordOwner cannot be null.");
        if (passwordToCheck == null) throw new IllegalArgumentException("Argument passwordToCheck cannot be null.");
        return BCrypt.checkpw(passwordToCheck, passwordOwner.getPasswordHash());
    }

    @Override
    public boolean isBanned(Account account) {
        return account.getAccountStatus().getStatusName().equalsIgnoreCase(AccountStatus.BANNED);
    }

    @Override
    public boolean isModerator(Account account) {
        return account.getAccountType().getTypeName().equalsIgnoreCase(AccountType.MODERATOR);
    }

    @Override
    public boolean hasRightsToSearch(Account searcher, Account target, boolean onlyPublished) {
        if (searcher == null) throw new IllegalArgumentException("Argument searcher cannot be null.");
        return isModerator(searcher) || onlyPublished || target != null && searcher.equals(target);
    }

    @Override
    public boolean hasRightsToView(Account viewer, InternshipOffer offer) {
        if (viewer == null) throw new IllegalArgumentException("Argument viewer cannot be null.");
        if (offer == null) throw new IllegalArgumentException("Argument offer cannot be null.");
        return isPublished(offer) || isModerator(viewer) || viewer.equals(offer.getAccount());
    }

    @Override
    public boolean hasRightsToEdit(Account editor, InternshipOffer offer) {
        if (editor == null) throw new IllegalArgumentException("Argument editor cannot be null.");
        if (offer == null) throw new IllegalArgumentException("Argument offer cannot be null.");
        return editor.equals(offer.getAccount()) || isModerator(editor);
    }
}