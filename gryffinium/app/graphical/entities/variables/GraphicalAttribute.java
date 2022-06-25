package graphical.entities.variables;

public class GraphicalAttribute extends GraphicalVariable
{
    private Boolean isStatic;
    private String visibility;

    public Boolean isStatic()
    {
        return isStatic;
    }

    public void setStatic(Boolean aStatic)
    {
        isStatic = aStatic;
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
