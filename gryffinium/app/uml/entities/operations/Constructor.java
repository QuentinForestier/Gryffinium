package uml.entities.operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.entities.operations.MethodDto;
import dto.entities.operations.OperationDto;
import play.libs.Json;
import uml.ClassDiagram;
import uml.entities.Entity;

public class Constructor extends Operation
{
    public Constructor(){
        super();
    }

    public Constructor(String name)
    {
        super(name);
    }

    public Constructor(OperationDto go, ClassDiagram cd)
    {
        super(go, cd);
    }

    @Override
    public OperationDto toDto(Entity e)
    {
        return new OperationDto(this, e);
    }

    @Override
    public ArrayNode getCreationCommand(Entity e)
    {
        ArrayNode result = Json.newArray();
        result.add(Command.createResponse(toDto(e), ElementTypeDto.CONSTRUCTOR));
        result.addAll(getParametersCreationCommands(e));
        return result;
    }

}
