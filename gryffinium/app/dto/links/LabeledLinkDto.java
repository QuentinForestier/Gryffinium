package dto.links;

import com.fasterxml.jackson.databind.JsonNode;
import uml.links.LabeledLink;

public class LabeledLinkDto extends LinkDto
{
    private Double distance;
    private JsonNode offset;
    private String name;

    public LabeledLinkDto(){

    }

    public LabeledLinkDto(LabeledLink link){
        super(link);
        this.distance = link.getLabel().getDistance();
        this.offset = link.getLabel().getOffset();
        this.name = link.getLabel().getName();
    }

    public Double getDistance()
    {
        return distance;
    }

    public void setDistance(Double distance)
    {
        this.distance = distance;
    }

    public JsonNode getOffset()
    {
        return offset;
    }

    public void setOffset(JsonNode offset)
    {
        this.offset = offset;
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
