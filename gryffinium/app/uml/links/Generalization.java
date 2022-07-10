package uml.links;

import dto.links.LinkDto;
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

    public Generalization(dto.links.LinkDto gl, ClassDiagram cd)
    {
        super(gl, cd);
        setGraphical(gl, cd);
    }

    public void setGraphical(LinkDto gl, ClassDiagram cd)
    {
        if (gl.getSourceId() != null)
        {
            try
            {
                this.child = (Class) cd.getEntity(gl.getSourceId());
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException(cd.getEntity(gl.getSourceId()).getName() + " is not a class");
            }
        }
        if (gl.getTargetId() != null)
        {
            try
            {
                this.parent = (Class) cd.getEntity(gl.getTargetId());
            }
            catch (ClassCastException e)
            {
                throw new IllegalArgumentException(cd.getEntity(gl.getTargetId()).getName() + " is not a class");
            }
        }
    }
}
