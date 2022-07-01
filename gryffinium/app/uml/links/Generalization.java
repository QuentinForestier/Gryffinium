package uml.links;

import graphical.links.GraphicalLink;
import uml.ClassDiagram;
import uml.entities.Class;

public class Generalization extends ClassRelationship
{
    private Class parent;
    private Class child;


    public Generalization(Class parent, Class child)
    {
        this.parent = parent;
        this.child = child;
    }

    public Generalization(GraphicalLink gl, ClassDiagram cd)
    {
        super(gl, cd);
        setGraphical(gl, cd);
    }

    public void setGraphical(GraphicalLink gl, ClassDiagram cd)
    {
        if (gl.getSourceId() != null)
        {
            this.child = (Class) cd.getEntity(gl.getSourceId());
        }
        if (gl.getTargetId() != null)
        {
            this.parent = (Class) cd.getEntity(gl.getTargetId());
        }
    }
}
