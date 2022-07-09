package dto.entities;

import uml.entities.AssociationClass;
import uml.entities.Class;

public class AssociationClassDto extends ClassDto
{
    public AssociationClassDto(AssociationClass e)
    {
        super(e);
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

    private Integer source;
    private Integer target;
}
