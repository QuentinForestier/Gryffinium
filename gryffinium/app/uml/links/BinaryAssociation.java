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
import uml.links.components.Multiplicity;
import uml.links.components.Role;

import javax.xml.bind.annotation.*;

@XmlSeeAlso({Aggregation.class})
@XmlType(name = "BinaryAssociation")
public class BinaryAssociation extends Association
{
    private boolean isDirected;

    private Role source;
    private Role target;

    public BinaryAssociation(){
        super();
    }

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

        fromDto(gba, cd);
    }


    @Override
    public Role getRole(String id)
    {
        if (this.source.getId().equals(id))
            return this.source;
        else if (this.target.getId().equals(id))
            return this.target;
        else
            return null;
    }


    public void fromDto(BinaryAssociationDto dto, ClassDiagram cd)
    {
        super.fromDto(dto, cd);
        if (dto.isDirected() != null)
            this.isDirected = dto.isDirected();

        if (dto.getSourceId() != null && this.source != null)
        {
            this.source.setEntity(cd.getEntity(dto.getSourceId()));
        }

        if (dto.getTargetId() != null && this.target != null)
        {
            this.target.setEntity(cd.getEntity(dto.getTargetId()));
        }

    }

    @XmlAttribute
    public boolean isDirected()
    {
        return isDirected;
    }

    public void setDirected(boolean directed)
    {
        isDirected = directed;
    }

    @XmlElement
    public Role getSource()
    {
        return source;
    }

    public void setSource(Role source)
    {
        this.source = source;
    }

    @XmlElement
    public Role getTarget()
    {
        return target;
    }

    public void setTarget(Role target)
    {
        this.target = target;
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
