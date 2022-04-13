package models;

import com.fasterxml.jackson.databind.JsonNode;
import models.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class Project
{
    private List<ProjectUser> users = new ArrayList<>();

    public void executeCommand(Command command, ProjectUser sender)
    {
        JsonNode response = command.execute(this, sender);
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
