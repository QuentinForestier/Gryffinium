package models;

import actors.UserActor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.chat.ChatMessageCommand;
import commands.Command;
import commands.uml.CreateCommand;
import commands.uml.UpdateCommand;
import graphical.GraphicalElementType;
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

    @Embeddable
    public static class ProjectUserId
    {
        @Column(name = "project_id")
        public UUID projectId;

        @Column(name = "user_id")
        public UUID userId;


        public ProjectUserId(UUID projectId,
                             UUID userId)
        {
            this.projectId = projectId;
            this.userId = userId;
        }

        public ProjectUserId()
        {

        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProjectUserId that = (ProjectUserId) o;
            return Objects.equals(projectId, that.projectId) && Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(projectId, userId);
        }
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

        if(actor == null){
            project.checkConnectedUsers();
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
        switch (message.get("type").asText())
        {
            case "ChatMessageCommand":
                ((ObjectNode) message).put("sender", this.user.getName());
                cmd = Json.fromJson(message, ChatMessageCommand.class);

                break;
            case "CreateCommand":
                cmd = new CreateCommand(message.get("data"),
                        GraphicalElementType.valueOf(message.get("entityType").asText()));
                break;
            case "UpdateCommand":
                cmd = new UpdateCommand(message.get("data"),
                        GraphicalElementType.valueOf(message.get("entityType").asText()),
                        (ObjectNode)message.get("reference"));
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

    public void disconnect(){
       if(actor != null)
        {
            actor.disconnect();
        }
    }

}
