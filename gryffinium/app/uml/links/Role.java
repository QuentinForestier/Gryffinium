package uml.links;

import com.fasterxml.jackson.databind.JsonNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.links.RoleDto;
import uml.ClassDiagram;
import uml.entities.Entity;

import java.awt.*;

public class Role
{
    private Multiplicity multiplicity;
    private String name;

    private Double distanceName;
    private JsonNode offsetName;
    private Double distanceMultiplicity;
    private JsonNode offsetMultiplicity;

    private Entity entity;

    public Multiplicity getMultiplicity()
    {
        return multiplicity;
    }

    public void setMultiplicity(Multiplicity multiplicity)
    {
        this.multiplicity = multiplicity;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Entity getEntity()
    {
        return entity;
    }

    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }


    public Double getDistanceName()
    {
        return distanceName;
    }

    public void setDistanceName(double distanceName)
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

    public void setDistanceMultiplicity(double distanceMultiplicity)
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

    public Role(String name, Multiplicity multiplicity, Entity entity)
    {
        this.entity = entity;
        this.multiplicity = multiplicity;
        this.name = name;
    }

    public Role(RoleDto rdto, ClassDiagram cd){
        setGraphical(rdto, cd);
    }

    public void setDistanceName(Double distanceName)
    {
        this.distanceName = distanceName;
    }

    public void setDistanceMultiplicity(Double distanceMultiplicity)
    {
        this.distanceMultiplicity = distanceMultiplicity;
    }

    public void setGraphical(RoleDto rdto, ClassDiagram cd)
    {
        if(rdto.getName() != null)
            this.name = rdto.getName();

        if(rdto.getMultiplicity() != null)
        {
            String[] bound = rdto.getMultiplicity().split("[.][.]");
            this.setMultiplicity(new Multiplicity(bound[0].charAt(0),
                    bound.length < 2 ? bound[0].charAt(0) :
                            bound[1].charAt(0)));
        }
        if(rdto.getDistanceName() != null)
            this.distanceName = rdto.getDistanceName();
        if(rdto.getOffsetName() != null)
            this.offsetName = rdto.getOffsetName();
        if(rdto.getDistanceMultiplicity() != null)
            this.distanceMultiplicity = rdto.getDistanceMultiplicity();
        if(rdto.getOffsetMultiplicity() != null)
            this.offsetMultiplicity = rdto.getOffsetMultiplicity();

        if(rdto.getElementId() != null)
            this.entity = cd.getEntity(rdto.getElementId());

    }

    public RoleDto toDto(Association association)
    {
        return new RoleDto(this, association);
    }

    public JsonNode getCreationCommands(Association association)
    {
        return Command.createResponse(toDto(association),
                ElementTypeDto.ROLE);
    }
}
