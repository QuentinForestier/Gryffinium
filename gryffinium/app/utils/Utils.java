package utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class Utils {
    public static ObjectNode createResponse(Object response, boolean ok) {
        ObjectNode result = Json.newObject();
        result.put("success", ok);
        if (response instanceof String) {
            result.put("body", (String) response);
        } else {
            result.putPOJO("body", response);
        }
        return result;
    }
}
