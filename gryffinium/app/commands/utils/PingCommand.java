package commands.utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.CommandType;
import models.Project;
import models.ProjectUser;
import org.joda.time.DateTime;
import play.libs.Json;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PingCommand implements Command
{
    @Override
    public ArrayNode execute(Project project)
    {
        return Json.newArray().add(Command.createResponse(Json.newObject().put("response", "ping"), null, CommandType.PING_COMMAND));
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
