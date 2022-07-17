package uml;

import com.fasterxml.jackson.databind.node.ArrayNode;
import play.libs.Json;
import uml.entities.Entity;
import uml.entities.operations.Operation;
import uml.entities.variables.Variable;
import uml.links.Association;
import uml.links.ClassRelationship;
import uml.links.Dependency;
import uml.types.ExistingTypes;
import uml.types.SimpleType;
import uml.types.Type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;

@XmlRootElement
public class ClassDiagram
{

    private final HashMap<String, Object> elements = new HashMap<>();

    private final ExistingTypes existingTypes = new ExistingTypes();

    @XmlElement(name = "entity")
    private final ArrayList<Entity> entities = new ArrayList<>();

    @XmlElement(name = "association")
    private final ArrayList<Association> associations = new ArrayList<>();

    @XmlElement
    private final ArrayList<Dependency> dependencies = new ArrayList<>();

    @XmlElement(name = "relationship")
    private final ArrayList<ClassRelationship> relationships = new ArrayList<>();

    public ExistingTypes getExistingTypes()
    {
        return existingTypes;
    }

    public void addEntity(Entity entity)
    {
        int id = 1;
        while (entity.getName().equals("") || entity.getName() == null || existingTypes.isTypeExisting(entity))
        {
            Type tmp = getExistingTypes().getTypeByName(entity.getName());
            if (tmp != null && getExistingTypes().isTypeExisting(tmp) && tmp instanceof SimpleType)
            {
                getExistingTypes().removeType(tmp);
                entity.getSubscribers().addAll(tmp.getSubscribers());
                continue;
            }
            entity.setName(entity.getClass().getSimpleName() + id++);
        }
        entities.add(entity);
        existingTypes.addType(entity);
        elements.put(entity.getId(), entity);
    }

    public Entity getEntity(String id)
    {
        Entity e = (Entity) elements.get(id);
        if (e == null)
            throw new IllegalArgumentException("Entity does not exist");
        return e;
    }

    public void removeEntity(Entity entity)
    {
        entities.remove(entity);
        existingTypes.removeType(entity);
        elements.remove(entity.getId());
    }

    public void addAssociation(Association association)
    {
        associations.add(association);
    }

    public Association getAssociation(String id)
    {
        return associations.stream().filter(a -> a.getId().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("Association not found"));
    }

    public void removeAssociation(Association association)
    {
        associations.remove(association);
    }

    public void addDependency(Dependency dependency)
    {
        dependencies.add(dependency);
    }

    public Dependency getDependency(String id)
    {
        return dependencies.stream().filter(d -> d.getId().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("Dependency not found"));
    }

    public void removeDependency(Dependency dependency)
    {
        dependencies.remove(dependency);
    }

    public void addRelationship(ClassRelationship relationship)
    {
        relationships.add(relationship);
    }

    public ClassRelationship getRelationship(String id)
    {
        return relationships.stream().filter(r -> r.getId().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("Relationship not found"));
    }

    public void removeRelationship(ClassRelationship relationship)
    {
        relationships.remove(relationship);
    }

    public ArrayNode getCreationCommands()
    {
        ArrayNode commands = Json.newArray();
        for (Entity e : entities)
        {
            commands.addAll(e.getCreationCommands());
        }

        for (Association a : associations)
        {
            commands.addAll(a.getCreationCommands());
        }

        for (Dependency d : dependencies)
        {
            commands.add(d.getCreationCommands());
        }

        for (ClassRelationship r : relationships)
        {
            commands.add(r.getCreationCommands());
        }

        return commands;
    }

    public void load()
    {
        for (Entity e : entities)
        {
            existingTypes.addType(e);
            elements.put(e.getId(), e);
        }

        for (Entity e : entities)
        {
            e.load(this);
        }

    }
}
