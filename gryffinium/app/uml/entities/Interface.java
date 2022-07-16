package uml.entities;


import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
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

    public Interface(EntityDto ge, ClassDiagram cd)
    {
        super(ge, cd);
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
        result.add(Command.createResponse(toDto(), ElementTypeDto.INTERFACE));
        result.addAll(getMethodsCreationCommands());
        result.addAll(getAttributesCreationCommands());
        return result;
    }
}
