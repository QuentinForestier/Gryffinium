package uml.links;

import uml.entities.Entity;

public class BinaryAssociation extends Association
{
    private boolean isDirected;

    private Role from;
    private Role to;

    public BinaryAssociation(Entity from, Entity to, String name,
                             boolean isDirected)
    {
        super(name);
        this.isDirected = isDirected;
        this.from = new Role(from.getName(), Multiplicity.N, from);
        this.to = new Role(to.getName(), Multiplicity.N, to);
    }

    public BinaryAssociation(Entity from, Entity to)
    {
        this(from, to, "", false);
    }

    public void swap()
    {
        Role tmp = from;
        from = to;
        to = tmp;
    }
}
