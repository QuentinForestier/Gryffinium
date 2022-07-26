package models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class ProjectUserId
{
    @Column(name = "project_id")
    public UUID projectId;

    @Column(name = "user_id")
    public UUID userId;


    public ProjectUserId(UUID projectId,
                         UUID userId)
    {
        this.projectId = projectId;
        this.userId = userId;
    }

    public ProjectUserId()
    {

    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectUserId that = (ProjectUserId) o;
        return Objects.equals(projectId, that.projectId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(projectId, userId);
    }
}
