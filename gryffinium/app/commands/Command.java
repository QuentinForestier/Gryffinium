package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import graphical.GraphicalElementType;
import models.Project;
import play.libs.Json;

public interface Command
{
    ArrayNode execute(Project project);


    default JsonNode createResponse(Object response, GraphicalElementType type)
    {
        return ((ObjectNode) Json.toJson(response)).put("elementType", type.toString());
    }
}
