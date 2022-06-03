package uml.entities.variables;

import uml.Visibility;

public class Attribut extends Variable
{
    private boolean isStatic;
    private Visibility visibility;

    public Attribut(String name, boolean isConstant, boolean isStatic, Visibility visibility)
    {
        super(name, isConstant);
        this.isStatic = isStatic;
        this.visibility = visibility;
    }

    public Attribut(String name)
    {
        this(name, false, false, Visibility.PRIVATE);
    }
}
