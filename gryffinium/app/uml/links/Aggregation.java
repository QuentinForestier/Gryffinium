package uml.links;

import uml.entities.Entity;

public class Aggregation extends BinaryAssociation
{
    public Aggregation(Entity from, Entity to, String name, boolean isDirected)
    {
        super(from, to, name, isDirected);
    }

    public Aggregation(Entity from, Entity to)
    {
        super(from, to);
    }
}
