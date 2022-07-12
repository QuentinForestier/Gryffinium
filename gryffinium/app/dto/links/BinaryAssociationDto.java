package dto.links;

import com.fasterxml.jackson.databind.JsonNode;
import uml.links.BinaryAssociation;

public class BinaryAssociationDto extends AssociationDto
{

    private Integer sourceId;

    private Integer targetId;
    private Boolean isDirected;

    private Double distance;
    private JsonNode offset;

    public BinaryAssociationDto()
    {
    }

    public BinaryAssociationDto(BinaryAssociation ba)
    {
        super(ba);
        this.isDirected = ba.isDirected();
        this.sourceId = ba.getSource().getEntity().getId();
        this.targetId = ba.getTarget().getEntity().getId();
        this.distance = ba.getDistance();
        this.offset = ba.getOffset();

    }

    public Boolean isDirected()
    {
        return isDirected;
    }

    public void setDirected(Boolean directed)
    {
        isDirected = directed;
    }

    public Integer getSourceId()
    {
        return sourceId;
    }

    public void setSourceId(Integer sourceId)
    {
        this.sourceId = sourceId;
    }

    public Integer getTargetId()
    {
        return targetId;
    }

    public void setTargetId(Integer targetId)
    {
        this.targetId = targetId;
    }

    public Boolean getDirected()
    {
        return isDirected;
    }

    public Double getDistance()
    {
        return distance;
    }

    public void setDistance(Double distance)
    {
        this.distance = distance;
    }

    public JsonNode getOffset()
    {
        return offset;
    }

    public void setOffset(JsonNode offset)
    {
        this.offset = offset;
    }
}
