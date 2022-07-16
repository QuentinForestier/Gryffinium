package uml.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.links.DependencyDto;
import dto.links.LabeledLinkDto;
import dto.links.LinkDto;
import play.libs.Json;
import uml.ClassDiagram;
import uml.entities.Entity;
import uml.links.components.Label;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="Dependency")
public class Dependency extends LabeledLink
{

    private Entity from;
    private Entity to;

    public Dependency(Entity from, Entity to, String name)
    {
        this.getLabel().setName(name);
        this.from = from;
        this.to = to;
    }

    public Dependency(Entity from, Entity to)
    {
        this(from, to, "");
    }

    public Dependency(DependencyDto dto, ClassDiagram diagram)
    {
        super();
        fromDto(dto, diagram);
    }

    public Entity getFrom()
    {
        return from;
    }

    public void setFrom(Entity from)
    {
        this.from = from;
    }

    public Entity getTo()
    {
        return to;
    }

    public void setTo(Entity to)
    {
        this.to = to;
    }

    public void fromDto(LabeledLinkDto dto, ClassDiagram cd)
    {
        super.fromDto(dto, cd);
        if (dto.getSourceId() != null)
        {
                this.from =  cd.getEntity(dto.getSourceId());
        }
        if(dto.getTargetId() != null)
        {
            this.to = cd.getEntity(dto.getTargetId());
        }
    }

    @Override
    public JsonNode getCreationCommands()
    {
        return Command.createResponse(toDto(),
                ElementTypeDto.DEPENDENCY);
    }

    @Override
    public LinkDto toDto()
    {
        return new DependencyDto(this);
    }
}
