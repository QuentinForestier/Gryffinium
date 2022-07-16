package uml.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.links.LinkDto;
import dto.links.RealizationDto;
import uml.ClassDiagram;
import uml.entities.Implementor;
import uml.entities.Interface;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Realization")
public class Realization extends ClassRelationship
{
    private Implementor implementor;
    private Interface interfce;

    public Realization(dto.links.LinkDto gl, ClassDiagram cd)
    {
        super();
        fromDto(gl, cd);
    }

    @XmlAnyElement
    public Implementor getImplementor()
    {
        return implementor;
    }

    public void setImplementor(Implementor implementor)
    {
        this.implementor = implementor;
    }

    @XmlElement(name = "interface")
    public Interface getInterface()
    {
        return interfce;
    }

    public void setInterface(Interface interfce)
    {
        this.interfce = interfce;
    }

    public void fromDto(LinkDto gl, ClassDiagram cd)
    {
        super.fromDto(gl, cd);
        if (gl.getSourceId() != null)
        {
            try
            {
                this.implementor = (Implementor) cd.getEntity(gl.getSourceId());
            }
            catch (ClassCastException e)
            {
                throw new IllegalArgumentException(cd.getEntity(gl.getSourceId()).getName() + " is not an implementor");
            }
        }
        if (gl.getTargetId() != null)
        {
            try
            {
                this.interfce = (Interface) cd.getEntity(gl.getTargetId());
            }
            catch (ClassCastException e)
            {
                throw new IllegalArgumentException(cd.getEntity(gl.getTargetId()).getName() + " is not an interface");
            }
        }
    }

    @Override
    public JsonNode getCreationCommands()
    {
        return Command.createResponse(toDto(),
                ElementTypeDto.REALIZATION);
    }

    @Override
    public LinkDto toDto()
    {
        return new RealizationDto(this);
    }
}
