package uml.entities;


import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.CommandType;
import dto.ElementTypeDto;
import dto.entities.EntityDto;
import play.libs.Json;
import uml.ClassDiagram;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="Interface")
public class Interface extends Entity implements Implementor
{
    public Interface()
    {
        super();
    }

    public Interface(String name)
    {
        super(name);
    }

    public Interface(Interface other)
    {
        this.setId(other.getId());
        this.setName(other.getName());
        this.setHeight(other.getHeight());
        this.setWidth(other.getWidth());
        this.setX(other.getX());
        this.setY(other.getY());
        this.setVisibility(other.getVisibility());
    }

    public Interface(EntityDto ge)
    {
        super(ge);
        fromDto(ge);
    }

    @Override
    public EntityDto toDto()
    {
        return new EntityDto(this);
    }

    @Override
    public ArrayNode getCreationCommands()
    {
        ArrayNode result = Json.newArray();
        result.add(Command.createResponse(toDto(), ElementTypeDto.INTERFACE, CommandType.SELECT_COMMAND));
        result.addAll(getMethodsCreationCommands());
        result.addAll(getAttributesCreationCommands());
        return result;
    }
}
