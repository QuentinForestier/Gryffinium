package dto.entities.variables;

import uml.entities.Entity;
import uml.entities.variables.Attribute;

public class AttributeDto extends VariableDto
{
    private Boolean isStatic;
    private String visibility;

    public AttributeDto(){}
    public AttributeDto(Attribute attribute, Entity parent)
    {
        super(attribute, parent);
        this.isStatic = attribute.isStatic();
        this.visibility = attribute.getVisibility().toString();
    }

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
