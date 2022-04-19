package commands;

import com.fasterxml.jackson.databind.JsonNode;
import models.Project;
import models.ProjectUser;
import play.libs.Json;

import java.util.ArrayList;

public class ChatMessageCommand implements Command
{
    private String message;
    private String sender = "";



    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    @Override
    public JsonNode execute(Project project, ProjectUser user)
    {
        return Json.toJson(this);
    }
}
