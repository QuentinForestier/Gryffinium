package dto.entities;

import uml.entities.Entity;
import uml.entities.InnerInterface;

public class InnerInterfaceDto extends EntityDto
{


    private Boolean isStatic;

    private String outer;

    public InnerInterfaceDto()
    {
        super();
    }

    public InnerInterfaceDto(InnerInterface e)
    {
        super(e);
        setStatic(e.isStatic());
    }

    public Boolean getStatic()
    {
        return isStatic;
    }

    public void setStatic(Boolean aStatic)
    {
        isStatic = aStatic;
    }

    public String getOuter()
    {
        return outer;
    }

    public void setOuter(String outer)
    {
        this.outer = outer;
    }
}
