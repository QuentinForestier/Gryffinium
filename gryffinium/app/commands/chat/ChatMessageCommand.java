package commands.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import models.Project;
import models.ProjectUser;
import play.libs.Json;

import java.util.ArrayList;

public class ChatMessageCommand implements Command
{
    private String message;

    private String sender;

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
    public JsonNode execute(Project project)
    {
        return Json.toJson(this);
    }
}
