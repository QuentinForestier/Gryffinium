package models;

import actors.UserActor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import models.commands.ChatMessageCommand;
import models.commands.Command;
import play.libs.Json;

public class ProjectUser
{
    private UserActor user;

    private Project project;


    public ProjectUser(Project project)
    {
        this.project = project;
        project.addUser(this);
    }


    public void setUser(UserActor user)
    {
        this.user = user;
    }

    public void handleMessage(JsonNode message) throws JsonProcessingException
    {

        Command cmd;
        switch(message.get("type").asText()){
            case "ChatMessage":

                cmd = Json.fromJson(message, ChatMessageCommand.class);
            break;
            default:
                throw new IllegalArgumentException("Unknown message type");
        }

        project.executeCommand(cmd, this);
    }

    public void send(JsonNode message){
        user.send(message);
    }
}
