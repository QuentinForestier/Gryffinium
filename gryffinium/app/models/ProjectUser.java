package models;

import actors.UserActor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.ChatMessageCommand;
import commands.Command;
import io.ebean.Model;
import io.ebean.annotation.NotNull;
import play.libs.Json;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ProjectUser extends Model {

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

    public ProjectUser(User user, Project project, boolean isOwner, boolean canWrite) {
        this.user = user;
        this.project = project;
        this.isOwner = isOwner;
        this.canWrite = canWrite;
    }

    public void setActor(UserActor actor)
    {
        this.actor = actor;
    }

    public UserActor getActor()
    {
        return actor;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public boolean getIsOwner() {
        return isOwner;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public boolean getCanWrite() {
        return canWrite;
    }

    public void handleMessage(JsonNode message)
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
        actor.send(message);
    }

    public JsonNode toJsonCollaborator(){
        ObjectNode json = Json.newObject();

        json.put("id", user.getId().toString());
        json.put("name", user.getName());
        json.put("email", user.getEmail());
        json.put("isOwner", isOwner);
        json.put("canWrite", canWrite);
        return json;
    }

}