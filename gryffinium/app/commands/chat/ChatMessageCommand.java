package commands.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import models.Project;
import models.ProjectUser;
import play.libs.Json;

public class ChatMessageCommand implements Command
{
    private String message;

    private String sender;


    public ChatMessageCommand(){

    }

    public ChatMessageCommand(JsonNode data)
    {
        this.message = data.get("message").asText();
        this.sender = data.get("sender").asText();
    }

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
    public ArrayNode execute(Project project)
    {
        return Json.newArray().add(Json.toJson(this));
    }

    @Override
    public Boolean canExecute(ProjectUser user)
    {
        return true;
    }
}
