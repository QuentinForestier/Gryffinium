package dto.links;

import com.fasterxml.jackson.databind.JsonNode;
import dto.ElementDto;
import uml.links.ClassRelationship;
import uml.links.Link;

public abstract class LinkDto extends ElementDto
{
    private String sourceId;

    private String targetId;

    private JsonNode vertices;

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

    public JsonNode getVertices()
    {
        return vertices;
    }

    public void setVertices(JsonNode vertices)
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

