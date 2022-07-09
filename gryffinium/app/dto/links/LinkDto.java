package dto.links;

import dto.ElementDto;

public class LinkDto extends ElementDto
{
    private Integer sourceId;

    private Integer targetId;

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
}

