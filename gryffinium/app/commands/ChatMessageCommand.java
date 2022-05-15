package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Project;
import models.ProjectUser;
import play.libs.Json;

import java.util.ArrayList;

public class ChatMessageCommand implements Command
{
    private String message;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public JsonNode execute(Project project, ProjectUser user)
    {
        ObjectNode json = (ObjectNode) Json.toJson(this);
        json.put("sender", user.getUser().getName());
        return json;
    }
}
