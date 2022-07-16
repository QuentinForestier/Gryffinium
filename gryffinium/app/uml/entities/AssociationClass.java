package uml.entities;

import uml.ClassDiagram;
import uml.links.BinaryAssociation;
import uml.links.components.Role;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="AssociationClass")
public class AssociationClass extends Class
{
    private BinaryAssociation association;

    public AssociationClass(String name, Entity source, Entity target)
    {
        this(name, new BinaryAssociation(source, target));
    }

    public AssociationClass(String name, BinaryAssociation association)
    {
        super(name);
        this.association = association;
    }

    public AssociationClass(dto.entities.AssociationClassDto gac, ClassDiagram cd)
    {
        super(gac, cd);
        if (gac.getSource() == null || gac.getTarget() == null)
        {
            throw new IllegalArgumentException(
                    "An association class must have a source and a target");
        }
        this.association = new BinaryAssociation(
                cd.getEntity(gac.getSource()),
                cd.getEntity(gac.getTarget())
        );
        setGraphical(gac, cd);
    }

    public void setGraphical(dto.entities.AssociationClassDto gac, ClassDiagram cd)
    {
        super.fromDto(gac, cd);
        if (gac.getSource() != null)
        {
            Entity source = cd.getEntity(gac.getSource());
            this.association.setSource(new Role(
                    source.getName(),
                    this.association.getSource().getMultiplicity(),
                    source
                    ));
        }

        if (gac.getTarget() != null)
        {
            Entity target = cd.getEntity(gac.getTarget());
            this.association.setTarget(new Role(target.getName(),
                    this.association.getTarget().getMultiplicity(), target));
        }
    }

}
