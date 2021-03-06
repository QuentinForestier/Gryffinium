package dto.entities.variables;

import dto.ElementDto;
import uml.entities.Entity;
import uml.entities.variables.Variable;

public class VariableDto extends ElementDto
{
    private String parentId;

    private String id;
    private String name;
    private Boolean isConstant;

    private String type;

    public VariableDto(){}
    public VariableDto(Variable variable, Entity parent){
        this.id = variable.getId();
        this.name = variable.getName();
        this.isConstant = variable.isConstant();
        this.type = variable.getType().getName();
        this.parentId = parent.getId();
    }

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean isConstant()
    {
        return isConstant;
    }

    public void setConstant(Boolean constant)
    {
        isConstant = constant;
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
