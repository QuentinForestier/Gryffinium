package uml.links;

import graphical.links.GraphicalLink;
import uml.ClassDiagram;

public class ClassRelationship
{
    private Integer id;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    ClassRelationship()
    {
    }

    ClassRelationship(GraphicalLink gl, ClassDiagram cd)
    {
        setGraphical(gl, cd);
    }

    public void setGraphical(GraphicalLink gl, ClassDiagram cd)
    {
        if (gl.getId() != null)
        {
            this.id = gl.getId();
        }
    }
}
