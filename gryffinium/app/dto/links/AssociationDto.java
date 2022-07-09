package dto.links;

import dto.ElementDto;
import uml.links.Association;

public class AssociationDto extends ElementDto
{
    private String name;
    public AssociationDto()
    {
    }
    public AssociationDto(Association association)
    {
        super(association.getId());
        this.name = association.getName();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}
