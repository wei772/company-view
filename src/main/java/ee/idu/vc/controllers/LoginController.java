package ee.idu.vc.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public JsonNode login(@RequestParam(value = "username") String username,
                          @RequestParam(value = "password") String password) {
        return tokenAsJson(username, "asdn203r0");
    }

    private JsonNode tokenAsJson(String username, String token) {
        return JsonNodeFactory.instance.objectNode().put("username", username).put("token", token);
    }
}
