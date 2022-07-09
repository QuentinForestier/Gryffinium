package dto.entities.operations;

import uml.entities.Entity;
import uml.entities.operations.Method;

public class MethodDto extends OperationDto
{
    private Boolean isAbstract;
    private Boolean isStatic;

    private String type;

    public MethodDto()
    {
    }

    public MethodDto(Method m, Entity parent)
    {
        super(m, parent);
        this.isAbstract = m.isAbstract();
        this.isStatic = m.isStatic();
        this.type = m.getReturnType().getName();
    }

    public Boolean isAbstract()
    {
        return isAbstract;
    }

    public void setAbstract(Boolean anAbstract)
    {
        isAbstract = anAbstract;
    }

    public Boolean isStatic()
    {
        return isStatic;
    }

    public void setStatic(Boolean aStatic)
    {
        isStatic = aStatic;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
