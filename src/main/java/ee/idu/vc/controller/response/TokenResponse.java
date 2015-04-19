package ee.idu.vc.controller.response;

public class TokenResponse implements JsonResponse {
    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean success() {
        return true;
    }
}
