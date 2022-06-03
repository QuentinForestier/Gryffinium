package uml.links;

import uml.entities.Entity;

public class Dependency
{
    private String name;

    private Entity from;
    private Entity to;

    public Dependency(Entity from, Entity to, String name)
    {
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public Dependency(Entity from, Entity to){
        this(from, to, "");
    }


}
