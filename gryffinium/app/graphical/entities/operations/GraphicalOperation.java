package graphical.entities.operations;

import graphical.GraphicalElement;

import java.util.List;

public class GraphicalOperation extends GraphicalElement
{
    private Integer parentId;

    private Integer id;
    private String name;

    private String visibility;


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
