package uml.entities;


import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.entities.EntityDto;
import play.libs.Json;

public class Interface extends Entity implements Implementor
{
    public Interface(String name)
    {
        super(name);
    }

    public Interface(dto.entities.EntityDto ge)
    {
        super(ge);
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
