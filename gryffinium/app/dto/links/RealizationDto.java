package dto.links;

import uml.links.Realization;

public class RealizationDto extends LinkDto
{


    public RealizationDto(){

    }

    public RealizationDto(Realization r){
        super(r);
        setTargetId(r.getInterface().getId());
        setSourceId(r.getImplementor().getId());
    }
}
