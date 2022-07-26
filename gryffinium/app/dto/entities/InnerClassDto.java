package dto.entities;

import uml.entities.Class;
import uml.entities.InnerClass;

public class InnerClassDto extends ClassDto
{
    private Boolean isStatic;

    private String outer;

    public InnerClassDto(){
        super();
    }

    public InnerClassDto(InnerClass ic)
    {
        super(ic);
        this.isStatic = ic.isStatic();
    }

    public Boolean isStatic()
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
