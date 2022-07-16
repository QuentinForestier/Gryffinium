package dto.links.components;

import com.fasterxml.jackson.databind.JsonNode;
import uml.links.Association;
import uml.links.components.Role;

public class RoleDto
{
    private String name;
    private String multiplicity;
    private Double distanceName;
    private JsonNode offsetName;
    private Double distanceMultiplicity;
    private JsonNode offsetMultiplicity;

    private String id;
    private String associationId;

    public RoleDto(){}

    public RoleDto(Role role, Association association){
        this.name = role.getName();
        this.multiplicity = role.getMultiplicity().toString();
        this.distanceName = role.getDistanceName();
        this.offsetName = role.getOffsetName();
        this.distanceMultiplicity = role.getDistanceMultiplicity();
        this.offsetMultiplicity = role.getOffsetMultiplicity();
        this.id = role.getId();
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

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAssociationId()
    {
        return associationId;
    }

    public void setAssociationId(String associationId)
    {
        this.associationId = associationId;
    }


}
