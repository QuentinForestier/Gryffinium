package uml.types;

public abstract class Type
{
    private String name;

    public Type(String name)
    {
        this.name = name;
        ExistingTypes.addType(this);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
