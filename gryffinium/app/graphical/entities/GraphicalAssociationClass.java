package graphical.entities;

public class GraphicalAssociationClass extends GraphicalClass
{
    public Integer getSource()
    {
        return source;
    }

    public void setSource(Integer source)
    {
        this.source = source;
    }

    public Integer getTarget()
    {
        return target;
    }

    public void setTarget(Integer target)
    {
        this.target = target;
    }

    private Integer source;
    private Integer target;
}
