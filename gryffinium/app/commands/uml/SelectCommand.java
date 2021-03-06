package commands.uml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.entities.*;
import models.Project;
import models.ProjectUser;
import play.libs.Json;

import java.util.List;

public class SelectCommand implements Command
{
    @Override
    public ArrayNode execute(Project project)
    {
        ArrayNode result = Json.newArray();

        result.addAll(project.getDiagram().getCreationCommands());

        return result;
    }

    @Override
    public Boolean canExecute(ProjectUser user)
    {
        return true;
    }

    @Override
    public List<ProjectUser> targets(Project project, ProjectUser user)
    {
        return List.of(user);
    }

}
