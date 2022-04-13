package models;

import actors.UserActor;
import com.fasterxml.jackson.databind.JsonNode;
import models.messages.Command;

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

    public void handleMessage(Command command){
        System.out.println("ProjectUser.handleMessage");
        project.handleMessage(command, this);
    }

    public void send(JsonNode message){
        user.send(message);
    }
}
