package ee.idu.vc.controller.response;

public class ItemIdResponse implements JsonResponse {
    private final Long id;

    public ItemIdResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean success() {
        return true;
    }
}