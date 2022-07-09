package dto.entities;

import uml.entities.Class;
import uml.entities.Entity;

public class ClassDto extends EntityDto
{
    private Boolean isAbstract;

    public ClassDto()
    {
    }
    public ClassDto(Class e)
    {
        super(e);
        setAbstract(e.isAbstract());

    }

    public Boolean isAbstract()
    {
        return isAbstract;
    }

    public void setAbstract(Boolean anAbstract)
    {
        isAbstract = anAbstract;
    }

}
