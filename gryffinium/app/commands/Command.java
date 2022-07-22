package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dto.ElementTypeDto;
import models.Project;
import models.ProjectUser;
import play.libs.Json;

public interface Command
{
    ArrayNode execute(Project project);

    Boolean canExecute(ProjectUser user);

    static JsonNode createResponse(Object response, ElementTypeDto type, CommandType cmd)
    {
        ObjectNode result = (ObjectNode) Json.toJson(response);

        if(type != null)
        {
            result.put("elementType", type.toString());
        }


        return result.put("commandType", cmd.toString());
    }
}
