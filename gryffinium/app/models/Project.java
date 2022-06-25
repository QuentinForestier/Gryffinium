package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Model;
import io.ebean.annotation.NotNull;
import play.libs.Json;
import play.libs.XML;
import commands.Command;
import uml.ClassDiagram;

import javax.persistence.*;
import java.util.*;

@Entity
public class Project extends Model
{
    @Id
    public UUID id;

    @NotNull
    public String name;

    @Transient
    public ClassDiagram diagram = new ClassDiagram();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public List<ProjectUser> projectUsers;

    public static final Map<UUID, Project> openProjects = new HashMap<>();


    public Project(String name)
    {
        this.name = name;
    }

    public Project(String name, User user)
    {
        this.name = name;
        this.projectUsers = new ArrayList<>();
        this.projectUsers.add(new ProjectUser(user, this, true, true));
    }

    public Project()
    {

    }

    public void setDiagram(ClassDiagram diagram)
    {
        this.diagram = diagram;
    }

    public ClassDiagram getDiagram()
    {
        return diagram;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public UUID getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<ProjectUser> getProjectUsers()
    {
        return projectUsers;
    }

    public void setProjectUsers(List<ProjectUser> projectUsers)
    {
        this.projectUsers = projectUsers;
    }

    public void addUser(ProjectUser user)
    {
        this.projectUsers.add(user);
    }

    public void executeCommand(Command command, ProjectUser sender)
    {
        sender = findProjectUser(sender.getUser().getId());
        if (sender == null || !sender.getCanWrite())
            return;
        ObjectNode response;
        try{
         response = (ObjectNode) command.execute(this);

        response.put("commandType", command.getClass().getSimpleName());
        }catch(Exception e){
          response = Json.newObject();
            response.put("type", "Error");
            response.put("message", e.getMessage());
            e.printStackTrace();
        }

        for (ProjectUser user : projectUsers)
        {
            if (user.getActor() != null)
                user.send(response);
        }
    }

    public void removeUser(ProjectUser user)
    {
        projectUsers.remove(user);
    }

    public JsonNode toJson()
    {
        ObjectNode json = Json.newObject();
        json.put("id", id.toString());
        json.put("name", name);
        json.putArray("collaborators");
        for (ProjectUser user : projectUsers)
        {
            json.withArray("collaborators").add(user.toJsonCollaborator());
        }
        return json;
    }

    public ProjectUser findProjectUser(UUID userId)
    {
        for (ProjectUser user : projectUsers)
        {
            if (user.getUser().getId().equals(userId))
                return user;
        }

        return null;
    }

    public ProjectUser getOwner()
    {
        return projectUsers.stream().filter(ProjectUser::getIsOwner).findFirst().get();
    }

    public void close(){
        for(ProjectUser user : projectUsers){
            user.disconnect();
        }

        Project.openProjects.remove(this.id);
    }

    public void checkConnectedUsers(){
        if(this.projectUsers.stream().filter(pu -> pu.getActor() != null).count() == 0){
            this.close();
        }
    }
}
