package uml.entities;

import uml.ClassDiagram;
import uml.links.Inner;

public class InnerClass extends Class implements InnerEntity
{
    private Inner inner;
    private boolean isStatic;

    public InnerClass(String name, Entity outer, boolean isStatic)
    {
        super(name);
        this.isStatic = isStatic;
        inner = new Inner(outer, this);
    }

    public InnerClass(String name, Entity outer)
    {
        this(name, outer, false);
    }

    public InnerClass(dto.entities.InnerClassDto gic, ClassDiagram cd)
    {
        super(gic);
        if(gic.isStatic() == null)
        {
            throw new IllegalArgumentException("Static argument missing");
        }
        if(gic.getOuter() == null)
        {
            throw new IllegalArgumentException("Outer argument missing");
        }

        this.inner = new Inner(cd.getEntity(gic.getOuter()), this);

        setGraphical(gic, cd);
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

    public void setGraphical(dto.entities.InnerClassDto gic, ClassDiagram cd)
    {
        super.setGraphical(gic);
        if (gic.isStatic() != null)
            this.setStatic(gic.isStatic());

        if(gic.getOuter() != null)
            this.getInner().setOuter(cd.getEntity(gic.getOuter()));
    }
}
