package dto.links;

import uml.links.MultiAssociation;
import uml.links.UnaryAssociation;

import javax.xml.bind.annotation.XmlType;

public class UnaryAssociationDto extends AssociationDto
{
    public UnaryAssociationDto()
    {
    }

    public UnaryAssociationDto(UnaryAssociation ua)
    {
        super(ua);
        this.setTargetId(ua.getTarget().getEntity().getId());
        this.setSourceId(ua.getParent().getId());
    }
}
