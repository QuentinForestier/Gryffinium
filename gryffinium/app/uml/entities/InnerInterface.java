package uml.entities;

import dto.entities.InnerInterfaceDto;
import uml.ClassDiagram;
import uml.links.Inner;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="InnerInterface")
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

    public InnerInterface(dto.entities.InnerInterfaceDto gi, ClassDiagram cd)
    {
        super(gi);
        if(gi.getOuter() == null)
        {
            throw new IllegalArgumentException("Outer argument missing");
        }

        this.inner = new Inner(cd.getEntity(gi.getOuter()), this);
        setGraphical(gi, cd);
    }

    public void setGraphical(InnerInterfaceDto gi, ClassDiagram cd)
    {
        super.fromDto(gi);
        if(gi.getStatic() != null)
            this.setStatic(gi.getStatic());

        if(gi.getOuter() != null)
            this.getInner().setOuter(cd.getEntity(gi.getOuter()));
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
}
