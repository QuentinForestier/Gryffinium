package uml.entities;

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

    public boolean isAbstract()
    {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract)
    {
        isAbstract = anAbstract;
    }

}
