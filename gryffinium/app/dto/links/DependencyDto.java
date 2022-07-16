package dto.links;

import uml.links.Dependency;

public class DependencyDto extends LabeledLinkDto
{
    public DependencyDto()
    {
    }

    public DependencyDto(Dependency d)
    {
        super(d);
        setTargetId(d.getTo().getId());
        setSourceId(d.getFrom().getId());
    }
}
