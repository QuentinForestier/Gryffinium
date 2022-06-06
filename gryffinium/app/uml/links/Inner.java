package uml.links;

import uml.entities.Entity;
import uml.entities.InnerEntity;

public class Inner
{
    private Integer id;
    private Entity outer;
    private InnerEntity inner;

    public Inner(Entity outer, InnerEntity inner)
    {
        this.outer = outer;
        this.inner = inner;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Entity getOuter()
    {
        return outer;
    }

    public void setOuter(Entity outer)
    {
        this.outer = outer;
    }

    public InnerEntity getInner()
    {
        return inner;
    }

    public void setInner(InnerEntity inner)
    {
        this.inner = inner;
    }
}
