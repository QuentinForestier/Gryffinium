package dto.links;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import uml.links.BinaryAssociation;

public class BinaryAssociationDto extends AssociationDto
{

    private String sourceRoleId;

    private Boolean isDirected;

    public BinaryAssociationDto()
    {
    }

    public BinaryAssociationDto(BinaryAssociation ba)
    {
        super(ba);
        this.isDirected = ba.isDirected();
        this.setSourceId(ba.getSource().getEntity().getId());
        this.setTargetId(ba.getTarget().getEntity().getId());
        this.sourceRoleId = ba.getSource().getId();
    }


    public Boolean isDirected()
    {
        return isDirected;
    }

    public void setDirected(Boolean directed)
    {
        isDirected = directed;
    }


    public Boolean getDirected()
    {
        return isDirected;
    }

    public String getSourceRoleId()
    {
        return sourceRoleId;
    }

    public void setSourceRoleId(String sourceRoleId)
    {
        this.sourceRoleId = sourceRoleId;
    }

}
