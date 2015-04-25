package ee.idu.vc.service;

import ee.idu.vc.controller.form.InternshipOfferForm;
import ee.idu.vc.model.Account;
import ee.idu.vc.model.InternshipOffer;

import javax.transaction.Transactional;
import java.util.List;

public interface InternshipService {
    /**
     * Searches for all internships that have been created by the given account and contain the given keyword.
     * @param from Result start index.
     * @param to Result end index.
     * @param onlyPublished Set true to only search for internships that have been published.
     * @param account Set to a non-null value to search for internships created by this account.
     * @param keyword Set to a non-null value to search for internships that contain the given keyword.
     * @return List of internships.
     */
    @Transactional
    List<InternshipOffer> searchInternships(int from, int to, boolean onlyPublished, Account account, String keyword);

    /**
     * Searches for all internships that have been created by the given account.
     * @param from Result start index.
     * @param to Result end index.
     * @param onlyPublished True if only published internships shall be returned. False if all shall be returned.
     * @param account Non-null account value if account related internships shall be returned.
     * @return List of internships.
     */
    @Transactional
    List<InternshipOffer> searchInternships(int from, int to, boolean onlyPublished, Account account);

    /**
     * Searches all internships.
     * @param from Result index start.
     * @param to Result end index.
     * @param onlyPublished True if only published internships shall be returned. False if all shall be returned.
     * @return List of internships.
     */
    @Transactional
    List<InternshipOffer> searchInternships(int from, int to, boolean onlyPublished);

    /**
     * Gets the count of internships by doing a search query and returning the number of results.
     * @param onlyPublished Set true to search for only published internship offers.
     * @return Count of internships.
     */
    @Transactional
    int internshipSearchResultsCount(boolean onlyPublished);

    /**
     * Gets the count of internships by doing a search query and returning the number of results.
     * @param onlyPublished Set true to search for only published internship offers.
     * @param account Set to a non-null value to search for internships added by this account.
     * @return Count of internships.
     */
    @Transactional
    int internshipSearchResultsCount(boolean onlyPublished, Account account);

    /**
     * Gets the count of internships by doing a search query and returning the number of results.
     * @param onlyPublished Set true to search for only published internship offers.
     * @param account Set to a non-null value to search for internships added by this account.
     * @param keyword Set to a non-null value to search for internships containing this keyword.
     * @return Count of internships.
     */
    @Transactional
    int internshipSearchResultsCount(boolean onlyPublished, Account account, String keyword);

    /**
     * Creates a new offer based on the form and saves it to the database.
     * @param form Offer form.
     * @param creator Offer creator.
     * @return Internship that has been saved.
     */
    @Transactional
    InternshipOffer createAndSave(InternshipOfferForm form, Account creator);
}