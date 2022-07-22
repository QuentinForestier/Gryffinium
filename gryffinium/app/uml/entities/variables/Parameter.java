package uml.entities.variables;

import com.fasterxml.jackson.databind.JsonNode;
import commands.Command;
import commands.CommandType;
import dto.ElementTypeDto;
import dto.entities.variables.ParameterDto;
import dto.entities.variables.VariableDto;
import uml.ClassDiagram;
import uml.entities.Entity;
import uml.entities.operations.Operation;

import javax.xml.bind.annotation.XmlTransient;

public class Parameter extends Variable
{
    private Operation parentOperation;

    public Parameter()
    {
        super();
    }

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
            throw new IllegalArgumentException("Parameter doesnt have a " +
                    "parent");
        }
        fromDto(gp, cd);
    }

    @XmlTransient
    public Operation getParentOperation()
    {
        return parentOperation;
    }

    public void setParentOperation(Operation parentOperation)
    {
        this.parentOperation = parentOperation;
    }

    public VariableDto toDto(Entity e, Operation o)
    {
        if (getParent() == null && e != null)
        {
            setParent(e);
        }
        if (getParentOperation() == null && o != null)
        {
            setParentOperation(o);
        }

        return new ParameterDto(this, e, o);
    }

    public void fromDto(ParameterDto pdto, ClassDiagram cd)
    {
        super.fromDto(pdto, cd);

        if (pdto.getMethodId() != null)
        {
            setParentOperation(cd.getEntity(pdto.getParentId()).getOperationById(pdto.getMethodId()));
        }
    }

    public JsonNode getCreationCommand(Entity e, Operation o)
    {
        return Command.createResponse(toDto(e, o), ElementTypeDto.PARAMETER, CommandType.SELECT_COMMAND);
    }

    @Override
    public JsonNode getUpdateNameCommand()
    {
        ParameterDto dto = new ParameterDto();
        dto.setType(getType().getName());
        dto.setId(getId());
        dto.setParentId(getParent().getId());
        dto.setMethodId(getParentOperation().getId());

        return Command.createResponse(dto, ElementTypeDto.PARAMETER, CommandType.SELECT_COMMAND);
    }
}
