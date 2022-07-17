package dto.links.components;

import com.fasterxml.jackson.databind.JsonNode;
import uml.links.Association;
import uml.links.components.Role;

public class RoleDto
{
    private String name;
    private String multiplicity;
    private String distanceName;
    private String offsetName;
    private String distanceMultiplicity;
    private String offsetMultiplicity;

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

    public String getDistanceName()
    {
        return distanceName;
    }

    public void setDistanceName(String distanceName)
    {
        this.distanceName = distanceName;
    }

    public String getOffsetName()
    {
        return offsetName;
    }

    public void setOffsetName(String offsetName)
    {
        this.offsetName = offsetName;
    }

    public String getDistanceMultiplicity()
    {
        return distanceMultiplicity;
    }

    public void setDistanceMultiplicity(String distanceMultiplicity)
    {
        this.distanceMultiplicity = distanceMultiplicity;
    }

    public String getOffsetMultiplicity()
    {
        return offsetMultiplicity;
    }

    public void setOffsetMultiplicity(String offsetMultiplicity)
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
