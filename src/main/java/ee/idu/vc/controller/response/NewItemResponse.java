package ee.idu.vc.controller.response;

public class NewItemResponse implements JsonResponse {
    private final Long id;

    public NewItemResponse(Long id) {
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