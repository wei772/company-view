package ee.idu.vc.controller.response;

import ee.idu.vc.model.InternshipOffer;

import java.util.ArrayList;
import java.util.List;

public class InternshipsSearchResponse {
    private final List<InternshipOffer> offers = new ArrayList<>();
    private int count;

    public InternshipsSearchResponse(List<InternshipOffer> offers, int count) {
        if (offers != null) addOffers(offers);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<InternshipOffer> getOffers() {
        return offers;
    }

    public void addOffers(List<InternshipOffer> offers) {
        this.offers.addAll(offers);
    }
}