package uml.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.links.AssociationDto;
import dto.links.BinaryAssociationDto;
import play.libs.Json;
import uml.ClassDiagram;
import uml.entities.Entity;

import java.awt.*;
import java.util.ArrayList;

public class BinaryAssociation extends Association
{
    private boolean isDirected;

    private double distance;
    private JsonNode offset;

    private Role source;
    private Role target;

    public BinaryAssociation(Entity source, Entity target, String name,
                             boolean isDirected)
    {
        super(name);
        this.isDirected = isDirected;
        this.source = new Role(source.getName(), Multiplicity.N, source);
        this.target = new Role(target.getName(), Multiplicity.N, target);
    }

    public BinaryAssociation(Entity source, Entity target)
    {
        this(source, target, "", false);
    }

    public BinaryAssociation(BinaryAssociationDto gba, ClassDiagram cd)
    {
        super(gba, cd);
        if (gba.getSourceId() == null)
        {
            throw new IllegalArgumentException("Source argument missing");
        }
        if (gba.getTargetId() == null)
        {
            throw new IllegalArgumentException("Target argument missing");
        }
        if (gba.isDirected() == null)
        {
            throw new IllegalArgumentException("Directed argument missing");
        }
        this.source = new Role(
                "",
                Multiplicity.N,
                cd.getEntity(gba.getSourceId()));

        this.target = new Role(
                "",
                Multiplicity.N,
                cd.getEntity(gba.getTargetId()));

        setGraphical(gba, cd);
    }


    @Override
    public Role getRoleByEntityId(Integer entityId)
    {
        if (this.source.getEntity().getId().equals(entityId))
            return this.source;
        else if (this.target.getEntity().getId().equals(entityId))
            return this.target;
        else
            return null;
    }


    @Override
    public void setGraphical(AssociationDto adto, ClassDiagram cd)
    {
        super.setGraphical(adto, cd);
        BinaryAssociationDto gba = (BinaryAssociationDto) adto;
        if (gba.isDirected() != null)
            this.isDirected = gba.isDirected();

        if (gba.getSourceId() != null && this.source != null)
        {
            Entity source = cd.getEntity(gba.getSourceId());
            this.source.setEntity(source);
        }


        if (gba.getTargetId() != null && this.target != null)
        {
            Entity target = cd.getEntity(gba.getTargetId());
            this.target.setEntity(target);
        }

        if(gba.getDistance() != null)
            this.distance = gba.getDistance();

        if(gba.getOffset() != null)
            this.offset = gba.getOffset();
    }

    public double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance)
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

    public boolean isDirected()
    {
        return isDirected;
    }

    public void setDirected(boolean directed)
    {
        isDirected = directed;
    }

    public Role getSource()
    {
        return source;
    }

    public void setSource(Role source)
    {
        this.source = source;
    }

    public Role getTarget()
    {
        return target;
    }

    public void setTarget(Role target)
    {
        this.target = target;
    }

    public void swap()
    {
        Role tmp = source;
        source = target;
        target = tmp;
    }

    @Override
    public AssociationDto toDto()
    {
        return new BinaryAssociationDto(this);
    }

    @Override
    public ArrayNode getCreationCommands()
    {
        ArrayNode result = Json.newArray();
        result.add(Command.createResponse(toDto(),
                ElementTypeDto.BINARY_ASSOCIATION));
        result.add(source.getCreationCommands(this));
        result.add(target.getCreationCommands(this));
        return result;
    }
}
