package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Project;
import play.libs.Json;

public interface Command
{
    ArrayNode execute(Project project);


    static JsonNode createResponse(Object response, dto.ElementTypeDto type)
    {
        return ((ObjectNode) Json.toJson(response)).put("elementType", type.toString());
    }
}
