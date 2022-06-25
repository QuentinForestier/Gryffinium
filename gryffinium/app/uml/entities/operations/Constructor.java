package uml.entities.operations;

import graphical.entities.operations.GraphicalOperation;
import uml.ClassDiagram;

public class Constructor extends Operation
{
    public Constructor(String name)
    {
        super(name);
    }

    public Constructor(GraphicalOperation go, ClassDiagram cd)
    {
        super(go,cd);
    }
}
