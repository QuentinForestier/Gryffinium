package uml.entities.variables;

import graphical.entities.variables.GraphicalParameter;
import graphical.entities.variables.GraphicalVariable;
import uml.ClassDiagram;

public class Parameter extends Variable
{
    public Parameter(String name, boolean isConstant)
    {
        super(name, isConstant);
    }

    public Parameter(String name)
    {
        super(name);
    }

    public Parameter(GraphicalParameter gp, ClassDiagram cd)
    {
        super(gp, cd);
        if(gp.getMethodId() == null)
        {
            throw new IllegalArgumentException("methodId attribute is null");
        }
        setGraphical(gp, cd);
    }

}
