package dto.entities;

import uml.entities.Entity;
import uml.entities.Enum;

import java.util.List;

public class EnumDto extends EntityDto
{
    private List<String> values;

    public EnumDto()
    {
    }
    public EnumDto(Enum e)
    {
        super(e);
    }

    public List<String> getValues()
    {
        return values;
    }

    public void setValues(List<String> values)
    {
        this.values = values;
    }
}
