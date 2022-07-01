package graphical.links;

public class GraphicalBinaryAssociation extends GraphicalAssociation
{
    private Boolean isDirected;

    private String sourceName;

    private String targetName;

    private String multiplicitySource;
    private String multiplicityTarget;

    public Boolean isDirected()
    {
        return isDirected;
    }

    public void setDirected(Boolean directed)
    {
        isDirected = directed;
    }

    public String getSourceName()
    {
        return sourceName;
    }

    public void setSourceName(String sourceName)
    {
        this.sourceName = sourceName;
    }

    public String getTargetName()
    {
        return targetName;
    }

    public void setTargetName(String targetName)
    {
        this.targetName = targetName;
    }

    public String getMultiplicitySource()
    {
        return multiplicitySource;
    }

    public void setMultiplicitySource(String multiplicitySource)
    {
        this.multiplicitySource = multiplicitySource;
    }

    public String getMultiplicityTarget()
    {
        return multiplicityTarget;
    }

    public void setMultiplicityTarget(String multiplicityTarget)
    {
        this.multiplicityTarget = multiplicityTarget;
    }
}
