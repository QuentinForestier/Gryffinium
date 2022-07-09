package dto.entities;

import uml.entities.Class;
import uml.entities.InnerClass;

public class InnerClassDto extends ClassDto
{
    private Boolean isStatic;

    private Integer outer;

    public InnerClassDto(InnerClass ic)
    {
        super(ic);
        // TODO
    }

    public Boolean isStatic()
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
