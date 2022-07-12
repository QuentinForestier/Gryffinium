package dto.links;

import com.fasterxml.jackson.databind.JsonNode;
import uml.links.Association;
import uml.links.Role;

import java.awt.*;

public class RoleDto
{
    private String name;
    private String multiplicity;
    private Double distanceName;
    private JsonNode offsetName;
    private Double distanceMultiplicity;
    private JsonNode offsetMultiplicity;

    private Integer elementId;
    private Integer associationId;

    public RoleDto(){}

    public RoleDto(Role role, Association association){
        this.name = role.getName();
        this.multiplicity = role.getMultiplicity().toString();
        this.distanceName = role.getDistanceName();
        this.offsetName = role.getOffsetName();
        this.distanceMultiplicity = role.getDistanceMultiplicity();
        this.offsetMultiplicity = role.getOffsetMultiplicity();
        this.elementId = role.getEntity().getId();
        this.associationId = association.getId();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMultiplicity()
    {
        return multiplicity;
    }

    public void setMultiplicity(String multiplicity)
    {
        this.multiplicity = multiplicity;
    }

    public Double getDistanceName()
    {
        return distanceName;
    }

    public void setDistanceName(Double distanceName)
    {
        this.distanceName = distanceName;
    }

    public JsonNode getOffsetName()
    {
        return offsetName;
    }

    public void setOffsetName(JsonNode offsetName)
    {
        this.offsetName = offsetName;
    }

    public Double getDistanceMultiplicity()
    {
        return distanceMultiplicity;
    }

    public void setDistanceMultiplicity(Double distanceMultiplicity)
    {
        this.distanceMultiplicity = distanceMultiplicity;
    }

    public JsonNode getOffsetMultiplicity()
    {
        return offsetMultiplicity;
    }

    public void setOffsetMultiplicity(JsonNode offsetMultiplicity)
    {
        this.offsetMultiplicity = offsetMultiplicity;
    }

    public Integer getElementId()
    {
        return elementId;
    }

    public void setElementId(Integer elementId)
    {
        this.elementId = elementId;
    }

    public Integer getAssociationId()
    {
        return associationId;
    }

    public void setAssociationId(Integer associationId)
    {
        this.associationId = associationId;
    }


}
