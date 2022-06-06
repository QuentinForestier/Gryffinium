package uml.entities.variables;

import uml.types.Type;

public abstract class Variable
{

    private Integer id;
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


    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isConstant()
    {
        return isConstant;
    }

    public void setConstant(boolean constant)
    {
        isConstant = constant;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
}
