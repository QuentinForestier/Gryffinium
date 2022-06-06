package graphical.entities;

public class GraphicalInnerInterface extends GraphicalEntity
{


    private Boolean isStatic;

    private Integer outer;

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
