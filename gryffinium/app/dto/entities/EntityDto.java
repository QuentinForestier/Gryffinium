package dto.entities;

import dto.ElementDto;
import uml.entities.Entity;

public class EntityDto extends ElementDto
{

    private Integer width;
    private Integer height;
    private Integer x;
    private Integer y;

    private String name;

    private String visibility;

    public EntityDto(){}

    public EntityDto(Entity e){
        super(e.getId());
        this.setId(e.getId());
        this.setWidth(e.getWidth());
        this.setHeight(e.getHeight());
        this.setX(e.getX());
        this.setY(e.getY());
        this.setVisibility(e.getVisibility().toString());
        this.setName(e.getName());
    }

    public String getVisibility()
    {
        return visibility;
    }

    public void setVisibility(String visibility)
    {
        this.visibility = visibility;
    }

    public Integer getWidth()
    {
        return width;
    }

    public void setWidth(Integer width)
    {
        this.width = width;
    }

    public Integer getHeight()
    {
        return height;
    }

    public void setHeight(Integer height)
    {
        this.height = height;
    }

    public Integer getX()
    {
        return x;
    }

    public void setX(Integer x)
    {
        this.x = x;
    }

    public Integer getY()
    {
        return y;
    }

    public void setY(Integer y)
    {
        this.y = y;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
