package uml.links.components;

import com.fasterxml.jackson.databind.JsonNode;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Label
{
    private String name;
    private Double distance;
    private JsonNode offset;

    @XmlAttribute
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlAttribute
    public Double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }

    @XmlElement
    public JsonNode getOffset()
    {
        return offset;
    }

    public void setOffset(JsonNode offset)
    {
        this.offset = offset;
    }
}
