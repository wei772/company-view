package ee.idu.vc.service;

import ee.idu.vc.model.Account;
import ee.idu.vc.model.InternshipOffer;

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

    @Transactional
    boolean isModerator(Account account);

    @Transactional
    boolean hasRightsToSearch(Account searcher, Account searchTargetAccount, boolean onlyPublished);

    @Transactional
    boolean hasRightsToView(Account viewer, InternshipOffer offer);

    @Transactional
    boolean hasRightsToEdit(Account editor, InternshipOffer offer);
}