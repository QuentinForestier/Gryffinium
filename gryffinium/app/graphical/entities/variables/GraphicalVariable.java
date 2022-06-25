package graphical.entities.variables;

import graphical.GraphicalElement;

public class GraphicalVariable extends GraphicalElement
{
    private Integer parentId;

    private Integer id;
    private String name;
    private Boolean isConstant;

    private String type;

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
