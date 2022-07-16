package dto.entities;

import uml.entities.AssociationClass;
import uml.entities.Class;

public class AssociationClassDto extends ClassDto
{
    public AssociationClassDto(AssociationClass e)
    {
        super(e);
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    private String source;
    private String target;
}
