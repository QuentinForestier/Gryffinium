package repository;

import models.Project;

import javax.inject.Inject;

public class ProjectRepository {
    private final DatabaseExecutionContext executionContext;

    @Inject
    public ProjectRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public void create(Project project) {
        project.save();
    }
}
