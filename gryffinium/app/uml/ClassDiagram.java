package uml;

import uml.entities.Entity;
import uml.entities.operations.Operation;
import uml.entities.variables.Variable;
import uml.links.Association;
import uml.links.ClassRelationship;
import uml.links.Dependency;
import uml.types.ExistingTypes;
import uml.types.SimpleType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;

@XmlRootElement
public class ClassDiagram
{

    private static int idCounter = 1;

    private HashMap<Integer, Object> elements = new HashMap<>();

    @XmlElement
    private ExistingTypes existingTypes = new ExistingTypes();

    private ArrayList<SimpleType> simpleTypes = new ArrayList<>();

    @XmlElement(name = "entity")
    private ArrayList<Entity> entities = new ArrayList<>();

    @XmlElement
    private ArrayList<Association> associations = new ArrayList<>();

    @XmlElement
    private ArrayList<Dependency> dependencies = new ArrayList<>();

    @XmlElement
    private ArrayList<ClassRelationship> relationships = new ArrayList<>();

    public ExistingTypes getExistingTypes()
    {
        return existingTypes;
    }

    public void addEntity(Entity entity)
    {
        entity.setId(idCounter++);
        if (entity.getName().equals("") || entity.getName() == null)
        {
            entity.setName(entity.getClass().getSimpleName() + " " + entity.getId());
        }
        entities.add(entity);
        existingTypes.addType(entity);
        elements.put(entity.getId(), entity);
    }

    public Entity getEntity(Integer id)
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
        association.setId(idCounter++);
        associations.add(association);
    }

    public Association getAssociation(Integer id)
    {
        return associations.stream().filter(a -> a.getId().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("Association not found"));
    }

    public void removeAssociation(Association association)
    {
        associations.remove(association);
    }

    public void addDependency(Dependency dependency)
    {
        dependency.setId(idCounter++);
        dependencies.add(dependency);
    }

    public Dependency getDependency(Integer id)
    {
        return dependencies.stream().filter(d -> d.getId().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("Dependency not found"));
    }

    public void removeDependency(Dependency dependency)
    {
        dependencies.remove(dependency);
    }

    public void addRelationship(ClassRelationship relationship)
    {
        relationship.setId(idCounter++);
        relationships.add(relationship);
    }

    public ClassRelationship getRelationship(Integer id)
    {
        return relationships.stream().filter(r -> r.getId().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("Relationship not found"));
    }

    public void removeRelationship(ClassRelationship relationship)
    {
        relationships.remove(relationship);
    }


}
