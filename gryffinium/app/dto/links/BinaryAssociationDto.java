package dto.links;

import uml.links.BinaryAssociation;

public class BinaryAssociationDto extends AssociationDto
{

    private Integer sourceId;

    private Integer targetId;
    private Boolean isDirected;

    private String sourceName;

    private String targetName;

    private String multiplicitySource;
    private String multiplicityTarget;

public BinaryAssociationDto(){}
    public BinaryAssociationDto(BinaryAssociation ba){
        super(ba);
        this.isDirected = ba.isDirected();
        this.sourceId = ba.getSource().getEntity().getId();
        this.sourceName = ba.getSource().getName();
        this.multiplicitySource = ba.getSource().getMultiplicity().toString();
        this.targetId = ba.getTarget().getEntity().getId();
        this.targetName = ba.getTarget().getName();
        this.multiplicityTarget = ba.getTarget().getMultiplicity().toString();
    }
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

    public Integer getSourceId()
    {
        return sourceId;
    }

    public void setSourceId(Integer sourceId)
    {
        this.sourceId = sourceId;
    }

    public Integer getTargetId()
    {
        return targetId;
    }

    public void setTargetId(Integer targetId)
    {
        this.targetId = targetId;
    }

    public Boolean getDirected()
    {
        return isDirected;
    }
}
