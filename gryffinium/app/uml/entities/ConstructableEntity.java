package uml.entities;

import uml.entities.operations.Constructor;

import java.util.ArrayList;

public abstract class ConstructableEntity extends Entity
{
    private final ArrayList<Constructor> constructors = new ArrayList<>();

    public ConstructableEntity(String name)
    {
        super(name);
    }
}
