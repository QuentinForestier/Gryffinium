package repository;

import io.ebean.DB;
import models.Project;
import models.User;

import javax.inject.Inject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class ProjectRepository {
    private final DatabaseExecutionContext executionContext;

    @Inject
    public ProjectRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public CompletionStage<Project> create(Project project) {
        return supplyAsync(() -> {
            project.save();
            return project;
        }, executionContext);
    }

    public Project findById(UUID id) {
        return supplyAsync(() -> DB.find(Project.class)
                .where()
                .eq("id", id)
                .findOne(), executionContext).join();
    }

    public List<Project> findProjectOfUser(UUID id) {
        return supplyAsync(() -> DB.find(Project.class)
                .fetch("projectUsers", "user")
                .where()
                .eq("user_id", id)
                .findList(), executionContext).join();
    }
}
