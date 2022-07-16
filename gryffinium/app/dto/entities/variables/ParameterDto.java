package dto.entities.variables;

import uml.entities.Entity;
import uml.entities.operations.Method;
import uml.entities.operations.Operation;
import uml.entities.variables.Parameter;

public class ParameterDto extends VariableDto
{
    private String methodId;

    public ParameterDto(){}
    public ParameterDto(Parameter parameter, Entity parent, Operation o)
    {
        super(parameter, parent);
        this.methodId = o.getId();
    }

    public String getMethodId()
    {
        return methodId;
    }

    public void setMethodId(String methodId)
    {
        this.methodId = methodId;
    }
}
