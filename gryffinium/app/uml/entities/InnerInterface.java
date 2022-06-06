package uml.entities;

import graphical.entities.GraphicalInnerInterface;
import uml.ClassDiagram;
import uml.links.Inner;

public class InnerInterface extends Interface implements InnerEntity
{

    private Inner inner;
    private boolean isStatic;

    public InnerInterface(String name, boolean isStatic){
        super(name);
        this.isStatic = isStatic;
    }

    public InnerInterface(String name)
    {
        this(name, false);
    }

    public InnerInterface(GraphicalInnerInterface gi, ClassDiagram cd)
    {
        super(gi);
        if(gi.getOuter() == null)
        {
            throw new IllegalArgumentException("Outer argument missing");
        }

        this.inner = new Inner(cd.getEntity(gi.getOuter()), this);
        setGraphical(gi, cd);
    }

    public void setGraphical(GraphicalInnerInterface gi, ClassDiagram cd)
    {
        super.setGraphical(gi);
        if(gi.getStatic() != null)
            this.setStatic(gi.getStatic());

        if(gi.getOuter() != null)
            this.getInner().setOuter(cd.getEntity(gi.getOuter()));
    }

    public Inner getInner()
    {
        return inner;
    }

    public void setInner(Inner inner)
    {
        this.inner = inner;
    }

    public boolean isStatic()
    {
        return isStatic;
    }

    public void setStatic(boolean aStatic)
    {
        isStatic = aStatic;
    }
}
