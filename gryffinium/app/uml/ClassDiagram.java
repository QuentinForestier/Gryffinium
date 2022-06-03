package uml;

import uml.entities.Entity;
import uml.entities.operations.Operation;
import uml.entities.variables.Variable;
import uml.links.Association;
import uml.links.ClassRelationship;
import uml.links.Dependency;
import uml.types.SimpleType;

import java.util.ArrayList;

public class ClassDiagram
{
    private ArrayList<SimpleType> simpleTypes = new ArrayList<>();

    private ArrayList<Entity> entities = new ArrayList<>();

    private ArrayList<Association> associations = new ArrayList<>();

    private ArrayList<Dependency> dependencies = new ArrayList<>();

    private ArrayList<ClassRelationship> relationships  = new ArrayList<>();

    private ArrayList<Variable> variables = new ArrayList<>();

    private ArrayList<Operation> operations = new ArrayList<>();


    public void addEntity(Entity entity)
    {
        entities.add(entity);
    }

    public Entity getEntity(String name)
    {
        return entities.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
    }

    public void removeEntity(Entity entity)
    {
        entities.remove(entity);
    }
}
