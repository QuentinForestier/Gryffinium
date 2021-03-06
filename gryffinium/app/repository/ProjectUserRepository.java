package repository;

import io.ebean.DB;
import models.Project;
import models.ProjectUser;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class ProjectUserRepository
{
    private final DatabaseExecutionContext executionContext;

    @Inject
    public ProjectUserRepository(DatabaseExecutionContext executionContext)
    {
        this.executionContext = executionContext;
    }

    public List<ProjectUser> getUsersOfProject(UUID projectId)
    {
        return supplyAsync(() -> DB.find(ProjectUser.class)
                .where().eq("project_id", projectId)
                .findList(), executionContext).join();
    }

    public CompletionStage<ProjectUser> save(ProjectUser pu)
    {
        return supplyAsync(() ->
        {
            DB.save(pu);
            return pu;
        }, executionContext);
    }

    public ProjectUser getUser(UUID userId, UUID projectId)
    {
        return supplyAsync(() -> DB.find(ProjectUser.class)
                .where().eq("user_id", userId)
                .eq("project_id", projectId)
                .findOne(), executionContext).join();
    }


    public void delete(ProjectUser user)
    {
        supplyAsync(() -> {
            DB.delete(user);
            return null;
        }, executionContext);

    }
}
