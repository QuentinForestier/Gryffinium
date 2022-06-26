package uml.entities.variables;

import uml.Visibility;
import uml.ClassDiagram;

import graphical.entities.variables.GraphicalAttribute;

import javax.xml.bind.annotation.XmlAttribute;

public class Attribute extends Variable
{

    private boolean isStatic;
    private Visibility visibility;

    public Attribute(String name, boolean isConstant, boolean isStatic, Visibility visibility)
    {
        super(name, isConstant);
        this.isStatic = isStatic;
        this.visibility = visibility;
    }

    public Attribute(GraphicalAttribute ga, ClassDiagram cd)
    {
        super(ga, cd);
        if(ga.isStatic() == null)
        {
            throw new IllegalArgumentException("isStatic attribute is null");
        }
        if(ga.getVisibility() == null)
        {
            throw new IllegalArgumentException("visibility attribute is null");
        }
        setGraphical(ga, cd);
    }

    public Attribute(String name)
    {
        this(name, false, false, Visibility.PRIVATE);
    }

    public void setGraphical(GraphicalAttribute ga, ClassDiagram cd)
    {
        super.setGraphical(ga, cd);
        if(ga.isStatic() != null)
            this.setStatic(ga.isStatic());
        if(ga.getVisibility() != null){
            this.setVisibility(Visibility.valueOf(ga.getVisibility().toUpperCase()));
        }
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

    @XmlAttribute
    public Visibility getVisibility()
    {
        return visibility;
    }

    public void setVisibility(Visibility visibility)
    {
        this.visibility = visibility;
    }
}
