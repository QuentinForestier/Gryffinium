package uml.links;

import uml.entities.Entity;

public class Role
{
    private Multiplicity multiplicity;
    private String name;

    private Entity entity;

    public Multiplicity getMultiplicity()
    {
        return multiplicity;
    }

    public void setMultiplicity(Multiplicity multiplicity)
    {
        this.multiplicity = multiplicity;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Entity getEntity()
    {
        return entity;
    }

    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }

    public Role(String name, Multiplicity multiplicity, Entity entity)
    {
        this.entity = entity;
        this.multiplicity = multiplicity;
        this.name = name;
    }
}
