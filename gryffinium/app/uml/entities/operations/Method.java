package uml.entities.operations;

import uml.types.Type;

public class Method extends Operation
{
    private boolean isAbstract;
    private boolean isStatic;

    private Type returnType = null;

    public Method(String name, boolean isAbstract, boolean isStatic){
        super(name);
        this.isAbstract =  isAbstract;
        this.isStatic = isStatic;
    }

    public Method(String name)
    {
        this(name, false, false);
    }
}
