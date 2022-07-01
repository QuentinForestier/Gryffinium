package uml.entities;

import graphical.entities.GraphicalEntity;
import uml.entities.operations.Constructor;
import uml.entities.operations.Operation;

import java.util.ArrayList;
import java.util.List;

public abstract class ConstructableEntity extends Entity
{
    private final ArrayList<Constructor> constructors = new ArrayList<>();

    public ConstructableEntity(String name)
    {
        super(name);
    }

    public ConstructableEntity(GraphicalEntity ge)
    {
        super(ge);
    }

    public void addConstructor(Constructor constructor)
    {
        constructor.setId(idCounter++);
        constructors.add(constructor);
    }

    public void removeConstructor(Constructor constructor)
    {
        constructors.remove(constructor);
    }

    public Constructor getConstructorById(int id)
    {
        return constructors.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public List<Constructor> getConstructors()
    {
        return constructors;
    }


    @Override
    public Operation getOperationById(Integer id)
    {
        Operation result = super.getOperationById(id);

        return result == null ? getConstructorById(id) : result;
    }
}
