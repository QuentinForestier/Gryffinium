package models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Model;
import io.ebean.annotation.NotNull;
import play.libs.Json;
import play.libs.XML;
import commands.Command;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Project extends Model
{
    @Id
    public UUID id;

    @NotNull
    public String name;

    public XML diagram;


    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public List<ProjectUser> projectUsers;

    public Project(String name)
    {
        this.name = name;
    }

    public void setDiagram(XML diagram)
    {
        this.diagram = diagram;
    }

    public XML getDiagram()
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
        JsonNode response = command.execute(this, sender);
        for (ProjectUser user : projectUsers)
        {
            if (user.getActor() != null)
                user.send(response);
        }
    }

    public void removeUser(UUID id)
    {
        projectUsers.stream().filter(pu -> pu.getUser().getId().equals(id)).forEach(user -> projectUsers.remove(user));
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
}
