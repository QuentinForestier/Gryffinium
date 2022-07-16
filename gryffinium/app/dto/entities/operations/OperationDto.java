package dto.entities.operations;

import dto.ElementDto;
import uml.entities.Entity;
import uml.entities.operations.Operation;

public class OperationDto extends ElementDto
{
    private String parentId;

    private String id;
    private String name;

    private String visibility;

    public OperationDto(){}
    public OperationDto(Operation operation, Entity parent)
    {
        this.id = operation.getId();
        this.name = operation.getName();
        this.visibility = operation.getVisibility().toString();
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

    public String getVisibility()
    {
        return visibility;
    }

    public void setVisibility(String visibility)
    {
        this.visibility = visibility;
    }

}
