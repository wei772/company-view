package ee.idu.vc.controller.response;

import ee.idu.vc.model.InternshipOffer;

import java.util.List;

public class InternshipsSearchResponse {
    private List<InternshipOffer> offers;
    private int count;

    public InternshipsSearchResponse(List offers, int count) {
        this.offers = offers;
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

    public void setOffers(List<InternshipOffer> offers) {
        this.offers = offers;
    }
}
