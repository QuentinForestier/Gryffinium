package uml.entities;

import graphical.entities.GraphicalClass;
import graphical.entities.GraphicalEntity;

public class Class extends ConstructableEntity implements Implementor
{
    private boolean isAbstract;

    public Class(String name, boolean isAbstract)
    {
        super(name);
        this.isAbstract = isAbstract;
    }

    public Class(String name)
    {
        this(name, false);
    }

    public Class(GraphicalClass gc)
    {
        super(gc);
        if(gc.isAbstract() == null)
        {
            this.setAbstract(false);
        }
        setGraphical(gc);
    }

    public boolean isAbstract()
    {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract)
    {
        isAbstract = anAbstract;
    }

    public void setGraphical(GraphicalClass ge)
    {
        super.setGraphical(ge);
        if(ge.isAbstract() != null)
            this.setAbstract(ge.isAbstract());
    }

}

