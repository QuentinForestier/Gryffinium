package graphical.entities.operations;

public class GraphicalMethod extends GraphicalOperation
{
    private Boolean isAbstract;
    private Boolean isStatic;

    private String type;

    public Boolean isAbstract()
    {
        return isAbstract;
    }

    public void setAbstract(Boolean anAbstract)
    {
        isAbstract = anAbstract;
    }

    public Boolean isStatic()
    {
        return isStatic;
    }

    public void setStatic(Boolean aStatic)
    {
        isStatic = aStatic;
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
