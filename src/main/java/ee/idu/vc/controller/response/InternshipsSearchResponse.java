package ee.idu.vc.controller.response;

import ee.idu.vc.model.InternshipOffer;
import ee.idu.vc.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class InternshipsSearchResponse {
    private final List<InternshipOffer> offers = new ArrayList<>();
    private int totalResults;

    public InternshipsSearchResponse(List<InternshipOffer> offers, int totalResults) {
        if (offers != null) addOffers(offers);
        this.totalResults = totalResults;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<InternshipOffer> getOffers() {
        return offers;
    }

    public void addOffers(List<InternshipOffer> offers) {
        this.offers.addAll(offers);
    }

    public int getResultsPerPageCount() {
        return Constants.RESULTS_PER_PAGE;
    }
}