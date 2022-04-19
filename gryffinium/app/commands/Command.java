package commands;

import com.fasterxml.jackson.databind.JsonNode;
import models.Project;
import models.ProjectUser;

public interface Command
{
    JsonNode execute(Project project, ProjectUser user);
}
