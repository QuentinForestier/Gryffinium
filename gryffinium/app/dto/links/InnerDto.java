package dto.links;

import uml.links.Inner;

public class InnerDto extends LinkDto
{
    public InnerDto()
    {
    }

    public InnerDto(Inner inner)
    {
        super(inner);
        setTargetId(inner.getOuter().getId());
        setSourceId(inner.getInner().getId());
    }
}
