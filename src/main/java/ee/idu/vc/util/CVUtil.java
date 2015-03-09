package ee.idu.vc.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CVUtil {
    public static JsonNode simpleJsonObject(String ... fieldsAndValues) {
        if (fieldsAndValues.length % 2 != 0) throw new IllegalArgumentException("Array length must be even");
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        for (int index = 0; index < fieldsAndValues.length; index += 2) {
            objectNode.put(fieldsAndValues[index], fieldsAndValues[index + 1]);
        }
        return objectNode;
    }
}
