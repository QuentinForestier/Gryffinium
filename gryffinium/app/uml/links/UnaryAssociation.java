package uml.links;

import com.fasterxml.jackson.databind.node.ArrayNode;
import dto.links.AssociationDto;
import uml.ClassDiagram;
import uml.links.components.Role;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "UnaryAssociation")
public class UnaryAssociation extends Association
{

    public UnaryAssociation()
    {
        super();
    }

    public UnaryAssociation(AssociationDto dto, ClassDiagram cd){
        super(dto, cd);
        fromDto(dto, cd);
    }

    public Role getRole(String id)
    {
        return getTarget().getId().equals(id) ? getTarget() : null;
    }

    @Override
    public AssociationDto toDto()
    {
        // TODO
        return null;
    }

    @Override
    public ArrayNode getCreationCommands()
    {
        // TODO
        return null;
    }
}
