package uml.entities;

import dto.entities.InnerClassDto;
import uml.ClassDiagram;
import uml.links.Inner;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "InnerClass")
public class InnerClass extends Class implements InnerEntity
{
    private Inner inner;
    private boolean isStatic;

    public InnerClass()
    {
        super();
    }

    public InnerClass(String name, Entity outer, boolean isStatic)
    {
        super(name);
        this.isStatic = isStatic;
        inner = new Inner(outer, this);
    }

    public InnerClass(Class other)
    {
        this.setId(other.getId());
        this.setName(other.getName());
        this.setAbstract(other.isAbstract());
        this.setHeight(other.getHeight());
        this.setWidth(other.getWidth());
        this.setX(other.getX());
        this.setY(other.getY());
        this.setVisibility(other.getVisibility());
        this.setStatic(false);

    }

    public InnerClass(String name, Entity outer)
    {
        this(name, outer, false);
    }

    public InnerClass(InnerClassDto gic, ClassDiagram cd)
    {
        super(gic);
        if (gic.isStatic() == null)
        {
            throw new IllegalArgumentException("Static argument missing");
        }
        if (gic.getOuter() == null)
        {
            throw new IllegalArgumentException("Outer argument missing");
        }

        this.inner = new Inner(cd.getEntity(gic.getOuter()), this);

        fromDto(gic, cd);
    }


    @XmlIDREF
    public Inner getInner()
    {
        return inner;
    }

    public void setInner(Inner inner)
    {
        this.inner = inner;
    }

    @XmlAttribute
    public boolean isStatic()
    {
        return isStatic;
    }

    public void setStatic(boolean aStatic)
    {
        isStatic = aStatic;
    }

    public void fromDto(InnerClassDto gic, ClassDiagram cd)
    {
        super.fromDto(gic);
        if (gic.isStatic() != null)
            this.setStatic(gic.isStatic());

        if (gic.getOuter() != null)
            this.getInner().setOuter(cd.getEntity(gic.getOuter()));
    }
}
