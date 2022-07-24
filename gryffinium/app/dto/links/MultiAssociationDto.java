package dto.links;

import com.fasterxml.jackson.databind.node.ArrayNode;
import play.libs.Json;
import uml.links.Link;
import uml.links.MultiAssociation;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MultiAssociationDto
{
    private String id;

    private ArrayNode targets;

    private Double x;
    private Double y;

    public MultiAssociationDto()
    {
    }

    public MultiAssociationDto(MultiAssociation ma){
        this.id = ma.getId();
        this.targets = (ArrayNode) Json.toJson(ma.getUnaryAssociations().stream().map(Link::getId).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
        this.setX(ma.getX());
        this.setY(ma.getY());
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public ArrayNode getTargets()
    {
        return targets;
    }

    public void setTargets(ArrayNode targets)
    {
        this.targets = targets;
    }

    public Double getX()
    {
        return x;
    }

    public void setX(Double x)
    {
        this.x = x;
    }

    public Double getY()
    {
        return y;
    }

    public void setY(Double y)
    {
        this.y = y;
    }
}
