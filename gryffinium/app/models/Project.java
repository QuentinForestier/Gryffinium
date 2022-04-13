package models;

import com.fasterxml.jackson.databind.JsonNode;
import models.messages.Command;

import java.util.ArrayList;
import java.util.List;

public class Project
{
    private List<ProjectUser> users = new ArrayList<>();

    public void handleMessage(Command command, ProjectUser sender)
    {
        System.out.println("Project.handleMessage");
        JsonNode response = command.execute();
        System.out.println(response);
        for (ProjectUser user : users)
        {
            user.send(response);
        }
    }

    public void addUser(ProjectUser user)
    {
        users.add(user);
    }
}
