package uml.links;

import com.fasterxml.jackson.databind.JsonNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.links.InnerDto;
import dto.links.LinkDto;
import uml.ClassDiagram;
import uml.entities.Entity;
import uml.entities.Implementor;
import uml.entities.InnerEntity;
import uml.entities.Interface;

import javax.xml.bind.annotation.*;

@XmlType(name = "Inner")
public class Inner extends Link
{
    private Entity outer;
    private InnerEntity inner;

    public Inner(Entity outer, InnerEntity inner)
    {
        this.outer = outer;
        this.inner = inner;
    }


    @XmlElement
    public Entity getOuter()
    {
        return outer;
    }

    public void setOuter(Entity outer)
    {
        this.outer = outer;
    }

    @XmlAnyElement
    public InnerEntity getInner()
    {
        return inner;
    }

    public void setInner(InnerEntity inner)
    {
        this.inner = inner;
    }

    @Override
    public void fromDto(LinkDto dto, ClassDiagram cd)
    {
        super.fromDto(dto, cd);
        if (dto.getSourceId() != null)
        {
            try
            {
                this.inner = (InnerEntity) cd.getEntity(dto.getSourceId());
            }
            catch (ClassCastException e)
            {
                throw new IllegalArgumentException(cd.getEntity(dto.getSourceId()).getName() + " is not a possible inner entity");
            }
        }
        if (dto.getTargetId() != null)
        {
            this.outer = cd.getEntity(dto.getTargetId());
            throw new IllegalArgumentException(cd.getEntity(dto.getTargetId()).getName() + " is not an interface");
        }
    }

    @Override
    public JsonNode getCreationCommands()
    {
        return Command.createResponse(toDto(),
                ElementTypeDto.INNER);
    }

    @Override
    public LinkDto toDto()
    {
        return new InnerDto(this);
    }
}
