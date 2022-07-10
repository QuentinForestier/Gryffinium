package uml.links;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.links.AssociationDto;
import dto.links.BinaryAssociationDto;
import play.libs.Json;
import uml.ClassDiagram;
import uml.entities.Entity;

public class BinaryAssociation extends Association
{
    private boolean isDirected;

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

        if (gba.getMultiplicitySource() != null && this.source != null)
        {
            // TODO check if bound is valid
            String[] bound = gba.getMultiplicitySource().split("[.][.]");
            this.source.setMultiplicity(new Multiplicity(bound[0].charAt(0),
                bound.length < 2 ? bound[0].charAt(0) : bound[1].charAt(0)));
        }

        if (gba.getTargetId() != null && this.target != null)
        {
            Entity target = cd.getEntity(gba.getTargetId());
            this.target.setEntity(target);
        }

        if (gba.getSourceName() != null && this.source != null)
            this.source.setName(gba.getSourceName());

        if (gba.getTargetName() != null && this.target != null)
            this.target.setName(gba.getTargetName());

        if (gba.getMultiplicityTarget() != null && this.target != null)
        {
            // TODO check if bound is valid
            String[] bound = gba.getMultiplicityTarget().split("[.][.]");
            this.target.setMultiplicity(new Multiplicity(bound[0].charAt(0),
                    bound.length < 2 ? bound[0].charAt(0) : bound[1].charAt(0)));
        }

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
        return result;
    }
}
