package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.uml.SelectCommand;
import io.ebean.Model;
import io.ebean.annotation.NotNull;
import play.libs.Json;
import play.libs.XML;
import commands.Command;
import uml.ClassDiagram;

import javax.persistence.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
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

    private Timer autoSaveTimer = new Timer();

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
        if (sender == null || !command.canExecute(sender))
            return;
        ObjectNode response = Json.newObject();
        try
        {
            response.set("commands", command.execute(this));
            for (ProjectUser user : command.targets(this, sender))
            {
                if (user.getActor() != null)
                    user.send(response);
            }
        }
        catch (Exception e)
        {

            response.put("commandType", "Error");
            response.put("message", e.getMessage());
            sender.send(response);
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

    public void close()
    {
        for (ProjectUser user : projectUsers)
        {
            user.disconnect();
        }

        autoSaveTimer.cancel();

        Project.openProjects.remove(this.id);
        this.saveDiagram();
    }

    public void checkConnectedUsers()
    {
        System.out.println("Checking connected users");
        if (this.projectUsers.stream().noneMatch(pu -> pu.getActor() != null))
        {
            this.close();
            System.out.println("Closing project");
        }
    }

    public void saveDiagram()
    {
        try
        {
            JAXBContext jaxbContext =
                    JAXBContext.newInstance(ClassDiagram.class);

            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter sw = new StringWriter();

            marshaller.marshal(getDiagram(), sw);

            String path = "./diagrams/";

            File directory = new File(path);
            if (!directory.exists())
            {
                directory.mkdir();
            }

            FileWriter fw = new FileWriter(path + getId() + ".xml");
            fw.write(sw.toString());
            fw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadDiagram()
    {
        try
        {
            JAXBContext jaxbContext =
                    JAXBContext.newInstance(ClassDiagram.class);

            File f = new File("./diagrams/" + getId() + ".xml");
            if (f.exists())
            {

                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                this.setDiagram((ClassDiagram) unmarshaller.unmarshal(f));

                getDiagram().load();
            }
            autoSaveTimer.scheduleAtFixedRate(new TimerTask()
            {
                @Override
                public void run()
                {
                    saveDiagram();
                    checkConnectedUsers();
                }
            }, 60 * 1000, 60 * 1000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
