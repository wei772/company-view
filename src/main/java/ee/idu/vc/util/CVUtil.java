package ee.idu.vc.util;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CVUtil {
    public static String ERROR_FIELDS = "errorFields", ERROR_MESSAGES = "errorMessages";

    public static boolean isStringEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }

    public static boolean isAnyStringEmpty(String ... strings) {
        for (String string : strings) if (isStringEmpty(string)) return true;
        return false;
    }

    public static <T> T tolerantCast(Class<T> type, Object object) {
        return object == null ? null : type.cast(object);
    }

    public static ArrayNode listToJsonArray(List objects) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        for (Object object : objects) arrayNode.add(object.toString());
        return arrayNode;
    }

    public static Map<String, List<String>> extractErrors(BindingResult bindingResult) {
        Map<String, List<String>> errors = new HashMap<>(2);
        List<String> errorFields = new ArrayList<>(bindingResult.getFieldErrors().size());
        List<String> errorMessages = new ArrayList<>(bindingResult.getFieldErrors().size());
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorFields.add(fieldError.getField());
            errorMessages.add(fieldError.getDefaultMessage());
        }
        errors.put(ERROR_FIELDS, errorFields);
        errors.put(ERROR_MESSAGES, errorMessages);
        return errors;
    }

    public static boolean containsErrors(Map<String, List<String>> errors) {
        return !errors.get(ERROR_FIELDS).isEmpty() || !errors.get(ERROR_MESSAGES).isEmpty();
    }

    public static ObjectNode jsonSimpleSuccessMessage() {
        return JsonNodeFactory.instance.objectNode().put("success", true);
    }

    public static ObjectNode jsonSimpleFailureMessage(String errorMessage) {
        return JsonNodeFactory.instance.objectNode().put("success", false).put("message", errorMessage);
    }

    public static ObjectNode jsonFailureMessageWithErrors(Map<String, List<String>> errors) {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode().put("success", false);
        objectNode.put("errorFields", listToJsonArray(errors.get(ERROR_FIELDS)));
        objectNode.put("errorMessages", listToJsonArray(errors.get(ERROR_MESSAGES)));
        return objectNode;
    }
}