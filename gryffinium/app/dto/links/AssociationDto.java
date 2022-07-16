package dto.links;

import com.fasterxml.jackson.databind.JsonNode;
import dto.ElementDto;
import uml.links.Association;

public class AssociationDto extends LabeledLinkDto
{

    private String targetRoleId;

    public AssociationDto()
    {
    }
    public AssociationDto(Association association)
    {
        super(association);
        this.targetRoleId = association.getTarget().getId();
    }

    public String getTargetRoleId()
    {
        return targetRoleId;
    }

    public void setTargetRoleId(String targetRoleId)
    {
        this.targetRoleId = targetRoleId;
    }
}
