package dto.entities;

import uml.entities.Entity;
import uml.entities.InnerInterface;

public class InnerInterfaceDto extends EntityDto
{


    private Boolean isStatic;

    private Integer outer;

    public InnerInterfaceDto(InnerInterface e)
    {
        super(e);
        // TODO
    }

    public Boolean getStatic()
    {
        return isStatic;
    }

    public void setStatic(Boolean aStatic)
    {
        isStatic = aStatic;
    }

    public Integer getOuter()
    {
        return outer;
    }

    public void setOuter(Integer outer)
    {
        this.outer = outer;
    }
}
