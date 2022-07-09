package uml.links;

import uml.ClassDiagram;
import uml.entities.Implementor;
import uml.entities.Interface;

public class Realization extends ClassRelationship
{
    private Implementor implementor;
    private Interface interfce;

    public Realization(dto.links.LinkDto gl, ClassDiagram cd)
    {
        super(gl, cd);
        setGraphical(gl, cd);
    }

    public Implementor getImplementor()
    {
        return implementor;
    }

    public void setImplementor(Implementor implementor)
    {
        this.implementor = implementor;
    }

    public Interface getInterface()
    {
        return interfce;
    }

    public void setInterface(Interface interfce)
    {
        this.interfce = interfce;
    }

    public void setGraphical(dto.links.LinkDto gl, ClassDiagram cd)
    {
        if (gl.getSourceId() != null)
        {
            this.implementor = (Implementor) cd.getEntity(gl.getSourceId());
        }
        if (gl.getTargetId() != null)
        {
            this.interfce = (Interface) cd.getEntity(gl.getTargetId());
        }
    }
}
