package uml.links;

import graphical.links.GraphicalAssociation;
import uml.entities.Entity;

public class Association
{
    private Integer id;
    private String name;

    public Association(String name)
    {
        this.name = name;
    }

    public Association(GraphicalAssociation graphicalAssociation)
    {
        setGraphical(graphicalAssociation);
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setGraphical(GraphicalAssociation ga)
    {
        if(ga.getName() != null)
            this.name = ga.getName();
    }
}
