package uml.links;

import com.fasterxml.jackson.databind.JsonNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.links.LinkDto;
import dto.links.GeneralizationDto;
import uml.ClassDiagram;
import uml.entities.Class;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Generalization")
public class Generalization extends ClassRelationship
{
    private Class parent;
    private Class child;

    public Generalization()
    {
        super();
    }

    public Generalization(Class parent, Class child)
    {
        this.parent = parent;
        this.child = child;
    }

    public Generalization(LinkDto gl, ClassDiagram cd)
    {
        super();
        fromDto(gl, cd);
    }

    public void fromDto(LinkDto gl, ClassDiagram cd)
    {
        super.fromDto(gl, cd);
        if (gl.getSourceId() != null)
        {
            try
            {
                this.child = (Class) cd.getEntity(gl.getSourceId());
            }
            catch (ClassCastException e)
            {
                throw new IllegalArgumentException(cd.getEntity(gl.getSourceId()).getName() + " is not a class");
            }
        }
        if (gl.getTargetId() != null)
        {
            try
            {
                this.parent = (Class) cd.getEntity(gl.getTargetId());
            }
            catch (ClassCastException e)
            {
                throw new IllegalArgumentException(cd.getEntity(gl.getTargetId()).getName() + " is not a class");
            }
        }
    }

    @Override
    public LinkDto toDto()
    {
        return new GeneralizationDto(this);
    }

    @Override
    public JsonNode getCreationCommands()
    {
        return Command.createResponse(toDto(), ElementTypeDto.GENERALIZATION);
    }

    @XmlIDREF
    public Class getParent()
    {
        return parent;
    }

    public void setParent(Class parent)
    {
        this.parent = parent;
    }

    @XmlIDREF
    public Class getChild()
    {
        return child;
    }

    public void setChild(Class child)
    {
        this.child = child;
    }
}
