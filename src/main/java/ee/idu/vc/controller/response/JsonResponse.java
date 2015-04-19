package ee.idu.vc.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface JsonResponse {
    @JsonProperty("success")
    public boolean success();
}
