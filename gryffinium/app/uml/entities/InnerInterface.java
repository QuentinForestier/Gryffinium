package uml.entities;

public class InnerInterface extends Interface implements InnerEntity
{
    private boolean isStatic;

    public InnerInterface(String name, boolean isStatic){
        super(name);
        this.isStatic = isStatic;
    }

    public InnerInterface(String name)
    {
        this(name, false);
    }
}
