package dto.links;

import com.fasterxml.jackson.databind.JsonNode;
import dto.ElementDto;
import play.libs.Json;
import uml.links.ClassRelationship;
import uml.links.Link;

public abstract class LinkDto extends ElementDto
{
    private String sourceId;

    private String targetId;

    private String vertices;

    public String getSourceId()
    {
        return sourceId;
    }

    public void setSourceId(String sourceId)
    {
        this.sourceId = sourceId;
    }

    public String getTargetId()
    {
        return targetId;
    }

    public void setTargetId(String targetId)
    {
        this.targetId = targetId;
    }

    public String getVertices()
    {
        return vertices;
    }

    public void setVertices(String vertices)
    {
        this.vertices = vertices;
    }

    public LinkDto()
    {
    }

    public LinkDto(Link l){
        super(l.getId());
        this.vertices = l.getVertices();
    }


}

