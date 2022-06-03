package uml.entities.variables;

import uml.types.Type;

public abstract class Variable
{
    private String name;
    private boolean isConstant;

    private Type type = null;

    public Variable(String name, boolean isConstant){
        this.name = name;
        this.isConstant = isConstant;
    }

    public Variable(String name){
        this(name, false);
    }


}
