package dto.links;

import dto.links.LinkDto;
import uml.links.Generalization;

public class GeneralizationDto extends LinkDto
{
    public GeneralizationDto()
    {
    }

    public GeneralizationDto(Generalization g)
    {
        super(g);
        setTargetId(g.getParent().getId());
        setSourceId(g.getChild().getId());
    }
}
