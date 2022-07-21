package uml.links;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.links.AssociationDto;
import dto.links.LinkDto;
import dto.links.UnaryAssociationDto;
import play.libs.Json;
import uml.ClassDiagram;
import uml.links.components.Multiplicity;
import uml.links.components.Role;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "UnaryAssociation")
public class UnaryAssociation extends Association
{
    private MultiAssociation parent;

    public UnaryAssociation()
    {
        super();
    }

    public UnaryAssociation(UnaryAssociationDto dto, ClassDiagram cd)
    {
        super(dto, cd);
        this.setTarget(new Role(
                "",
                Multiplicity.N,
                cd.getEntity(dto.getTargetId())));
        fromDto(dto, cd);
    }

    public MultiAssociation getParent()
    {
        return parent;
    }

    public void setParent(MultiAssociation parent)
    {
        this.parent = parent;
    }

    public Role getRole(String id)
    {
        return getTarget().getId().equals(id) ? getTarget() : null;
    }

    public AssociationDto toDto()
    {
        return new UnaryAssociationDto(this);
    }

    public void fromDto(UnaryAssociationDto dto, ClassDiagram cd)
    {
        super.fromDto(dto, cd);

        if (dto.getTargetId() != null && this.getTarget() != null)
        {
            this.getTarget().setEntity(cd.getEntity(dto.getTargetId()));
        }
    }

    public ArrayNode getCreationCommands()
    {
        ArrayNode result = Json.newArray();
        result.add(Command.createResponse(toDto(),
                ElementTypeDto.UNARY_ASSOCIATION));
        result.add(getTarget().getCreationCommands(this));
        return result;
    }
}
