package uml.entities.operations;

import graphical.entities.operations.GraphicalMethod;
import uml.ClassDiagram;
import uml.types.SimpleType;
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

    public Method(GraphicalMethod gm, ClassDiagram cd){
        super(gm, cd);
        if(gm.isAbstract() == null)
        {
            throw new IllegalArgumentException("isAbstract attribute is null");
        }
        if(gm.isStatic() == null)
        {
            throw new IllegalArgumentException("isStatic attribute is null");
        }
        setGraphical(gm, cd);
    }

    public boolean isAbstract()
    {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract)
    {
        isAbstract = anAbstract;
    }

    public boolean isStatic()
    {
        return isStatic;
    }

    public void setStatic(boolean aStatic)
    {
        isStatic = aStatic;
    }

    public Type getReturnType()
    {
        return returnType;
    }

    public void setReturnType(Type returnType)
    {
        this.returnType = returnType;
    }

    public void setGraphical(GraphicalMethod gm, ClassDiagram cd)
    {
        super.setGraphical(gm, cd);
        if(gm.isAbstract() != null)
            this.setAbstract(gm.isAbstract());
        if(gm.isStatic() != null)
            this.setStatic(gm.isStatic());
        if(gm.getType() != null)
        {
            this.returnType = cd.getExistingTypes().getTypeByName(gm.getType());
            if(returnType == null){
                this.returnType = new SimpleType(gm.getType());
                cd.getExistingTypes().addType(this.returnType);
            }
        }
    }
}
