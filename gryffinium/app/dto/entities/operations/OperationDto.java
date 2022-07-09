package dto.entities.operations;

import dto.ElementDto;
import uml.entities.Entity;
import uml.entities.operations.Operation;

public class OperationDto extends ElementDto
{
    private Integer parentId;

    private Integer id;
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

    public Integer getParentId()
    {
        return parentId;
    }

    public void setParentId(Integer parentId)
    {
        this.parentId = parentId;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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
