package uml.entities;


import graphical.entities.GraphicalEntity;

public class Interface extends Entity implements Implementor
{
    public Interface(String name)
    {
        super(name);
    }

    public Interface(GraphicalEntity ge)
    {
        super(ge);
    }
}
