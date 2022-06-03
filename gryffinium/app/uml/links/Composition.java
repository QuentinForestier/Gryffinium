package uml.links;

import uml.entities.Entity;

public class Composition extends Aggregation
{
    public Composition(Entity from, Entity to, String name, boolean isDirected)
    {
        super(from, to, name, isDirected);
    }

    public Composition(String name, Entity from, Entity to)
    {
        super(from, to);
    }
}
