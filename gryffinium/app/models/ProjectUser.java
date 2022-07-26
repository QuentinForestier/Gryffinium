package models;

import actors.UserActor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandType;
import commands.chat.ChatMessageCommand;
import commands.Command;
import commands.uml.CreateCommand;
import commands.uml.RemoveCommand;
import commands.uml.SelectCommand;
import commands.uml.UpdateCommand;
import commands.utils.PingCommand;
import io.ebean.Model;
import io.ebean.annotation.NotNull;
import play.libs.Json;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class ProjectUser extends Model
{

    public ProjectUser()
    {

    }


    @EmbeddedId
    public ProjectUserId id;

    @ManyToOne(optional = false)
    public User user;

    @ManyToOne(optional = false)
    @JsonBackReference
    public Project project;

    @NotNull
    public boolean isOwner;

    @NotNull
    public boolean canWrite;

    private UserActor actor;

    public ProjectUser(User user, Project project, boolean isOwner,
                       boolean canWrite)
    {
        this.user = user;
        this.project = project;
        this.isOwner = isOwner;
        this.canWrite = canWrite;
    }

    public void setActor(UserActor actor)
    {

        if (actor == null)
        {
            this.disconnect();
        }
        this.actor = actor;
    }

    public UserActor getActor()
    {
        return actor;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public User getUser()
    {
        return user;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    public Project getProject()
    {
        return project;
    }

    public void setIsOwner(boolean isOwner)
    {
        this.isOwner = isOwner;
    }

    public boolean getIsOwner()
    {
        return isOwner;
    }

    public void setCanWrite(boolean canWrite)
    {
        this.canWrite = canWrite;
    }

    public boolean getCanWrite()
    {
        return canWrite;
    }

    public void handleMessage(JsonNode message)
    {
        Command cmd;
        switch (CommandType.valueOf(message.get("type").asText()))
        {
            case CHAT_MESSAGE_COMMAND:
                ObjectNode data = (ObjectNode) message.get("data");
                cmd = new ChatMessageCommand(data.put("sender", user.getName()));
                break;
            case CREATE_COMMAND:
                cmd = new CreateCommand(message.get("data"),
                        dto.ElementTypeDto.valueOf(message.get("entityType").asText()));
                break;
            case UPDATE_COMMAND:
                cmd = new UpdateCommand(message.get("data"),
                        dto.ElementTypeDto.valueOf(message.get("entityType").asText()));
                break;
            case REMOVE_COMMAND:
                cmd = new RemoveCommand(message.get("data"),
                        dto.ElementTypeDto.valueOf(message.get("entityType").asText()));
                break;
            case SELECT_COMMAND:
                cmd = new SelectCommand();
                break;
            case PING_COMMAND:
                cmd = new PingCommand();
                break;
            default:
                throw new IllegalArgumentException("Unknown message type");
        }

        project.executeCommand(cmd, this);
    }

    public void send(JsonNode message)
    {
        actor.send(message);
    }

    public JsonNode toJsonCollaborator()
    {
        ObjectNode json = Json.newObject();

        json.put("id", user.getId().toString());
        json.put("name", user.getName());
        json.put("email", user.getEmail());
        json.put("isOwner", isOwner);
        json.put("canWrite", canWrite);
        return json;
    }

    public void disconnect()
    {
        if (actor != null)
        {
            actor.disconnect();
        }
    }

}
