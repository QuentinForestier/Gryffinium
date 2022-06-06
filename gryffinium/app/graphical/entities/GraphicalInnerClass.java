package graphical.entities;

public class GraphicalInnerClass extends GraphicalClass
{
    private Boolean isStatic;

    private Integer outer;

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
