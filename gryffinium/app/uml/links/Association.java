package uml.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dto.links.AssociationDto;
import uml.ClassDiagram;
import uml.links.components.Label;
import uml.links.components.Role;

import javax.xml.bind.annotation.*;

@XmlSeeAlso({BinaryAssociation.class, MultiAssociation.class})
public abstract class Association extends LabeledLink
{

    private Role target;

    public Association()
    {
        super();
    }

    public Association(String name)
    {
        super();

    }

    public Association(AssociationDto graphicalAssociation, ClassDiagram cd)
    {
        super();
        this.fromDto(graphicalAssociation, cd);
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

    @XmlTransient
    public String getDistance()
    {
        return this.getLabel().getDistance();
    }

    public void setDistance(String distance)
    {
        this.getLabel().setDistance(distance);
    }

    @XmlTransient
    public String getOffset()
    {
        return this.getLabel().getOffset();
    }

    public void setOffset(String offset)
    {
        this.getLabel().setOffset(offset);
    }

    @XmlTransient
    public String getName()
    {
        return getLabel().getName();
    }

    public void setName(String name)
    {
        this.getLabel().setName(name);
    }

    public void fromDto(AssociationDto ga, ClassDiagram cd)
    {
        super.fromDto(ga, cd);
        if (ga.getName() != null)
            this.getLabel().setName(ga.getName());

        if (ga.getDistance() != null)
            this.getLabel().setDistance(ga.getDistance());

        if (ga.getOffset() != null)
            this.getLabel().setOffset(ga.getOffset());
    }

    public abstract Role getRole(String id);

    public abstract AssociationDto toDto();

    public abstract ArrayNode getCreationCommands();
}
