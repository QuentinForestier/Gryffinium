package uml.entities.variables;

import uml.Visibility;

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

    public Attribute(String name)
    {
        this(name, false, false, Visibility.PRIVATE);
    }
}
