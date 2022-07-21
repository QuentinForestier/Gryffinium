package dto.links;

import uml.links.Link;
import uml.links.MultiAssociation;

import java.util.ArrayList;

public class MultiAssociationDto
{
    private String id;

    private ArrayList<String> unaryAssociations = new ArrayList<>();

    private Double x;
    private Double y;

    public MultiAssociationDto(MultiAssociation ma){
        this.id = ma.getId();
        this.unaryAssociations = ma.getUnaryAssociations().stream().map(Link::getId).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
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

    public ArrayList<String> getUnaryAssociations()
    {
        return unaryAssociations;
    }

    public void setUnaryAssociations(ArrayList<String> unaryAssociations)
    {
        this.unaryAssociations = unaryAssociations;
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
