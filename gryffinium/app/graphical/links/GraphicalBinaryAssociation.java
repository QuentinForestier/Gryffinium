package graphical.links;

import scala.Int;

public class GraphicalBinaryAssociation extends GraphicalAssociation
{
    private Boolean isDirected;

    private Integer source;
    private Integer target;

    private String sourceName;

    private String targetName;

    private String multiplicitySource;
    private String multiplicityTarget;

    public Boolean getDirected()
    {
        return isDirected;
    }

    public void setDirected(Boolean directed)
    {
        isDirected = directed;
    }

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
