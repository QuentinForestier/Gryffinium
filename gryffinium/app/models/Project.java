package models;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Model;
import io.ebean.annotation.NotNull;
import play.libs.XML;
import commands.Command;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Project extends Model {
    @Id
    private UUID id;

    @NotNull
    private String name;

    private XML diagram;


    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public List<ProjectUser> projectUsers;

    public Project(String name) {
        this.name = name;
    }

    public void setDiagram(XML diagram) {
        this.diagram = diagram;
    }

    public XML getDiagram() {
        return diagram;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addUser(ProjectUser user) {
        this.projectUsers.add(user);
    }

    public void executeCommand(Command command, ProjectUser sender) {
        JsonNode response = command.execute(this, sender);
        for (ProjectUser user : projectUsers) {
            if (user.getActor() != null)
                user.send(response);
        }
    }
}
