package uml.entities;

public class InnerClass extends Class implements InnerEntity
{
    private boolean isStatic;

    public InnerClass(String name, boolean isStatic)
    {
        super(name);
        this.isStatic = isStatic;
    }

    public InnerClass(String name)
    {
        this(name, false);
    }
}
