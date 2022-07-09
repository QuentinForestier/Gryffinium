package uml.entities.variables;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.entities.variables.ParameterDto;
import dto.entities.variables.VariableDto;
import play.libs.Json;
import uml.ClassDiagram;
import uml.entities.Entity;
import uml.entities.operations.Method;
import uml.entities.operations.Operation;

public class Parameter extends Variable
{
    public Parameter(String name, boolean isConstant)
    {
        super(name, isConstant);
    }

    public Parameter(String name)
    {
        super(name);
    }


    public Parameter(dto.entities.variables.ParameterDto gp, ClassDiagram cd)
    {
        super(gp, cd);
        if (gp.getMethodId() == null)
        {
            throw new IllegalArgumentException("methodId attribute is null");
        }
        setGraphical(gp, cd);
    }

    public VariableDto toDto(Entity e, Operation o)
    {
        return new ParameterDto(this, e, o);
    }

    public JsonNode getCreationCommand(Entity e, Operation o)
    {
        return Command.createResponse(toDto(e, o), ElementTypeDto.PARAMETER);
    }
}
