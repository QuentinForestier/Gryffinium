package uml;

import com.fasterxml.jackson.databind.node.ArrayNode;
import play.libs.Json;
import uml.entities.Entity;
import uml.links.*;
import uml.types.ExistingTypes;
import uml.types.SimpleType;
import uml.types.Type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class ClassDiagram
{


    private final ExistingTypes existingTypes = new ExistingTypes();

    @XmlElement(name = "entity")
    private final ArrayList<Entity> entities = new ArrayList<>();

    @XmlElement(name = "association")
    private final ArrayList<Association> associations = new ArrayList<>();

    @XmlElement(name = "dependency")
    private final ArrayList<Dependency> dependencies = new ArrayList<>();

    @XmlElement(name = "relationship")
    private final ArrayList<ClassRelationship> relationships =
            new ArrayList<>();

    @XmlElement(name = "inner")
    private final ArrayList<Inner> inners = new ArrayList<>();

    @XmlElement(name = "multiAssociation")
    private final ArrayList<MultiAssociation> multiAssociations =
            new ArrayList<>();

    @XmlElement(name="associationClass")
    private final ArrayList<AssociationClass> associationClasses = new ArrayList<>();


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
    }

    public Entity getEntity(String id)
    {
        Entity e =
                (Entity) entities.stream().filter(e1 -> e1.getId().equals(id)).findFirst().orElse(null);
        if (e == null)
            throw new IllegalArgumentException("Entity does not exist");
        return e;
    }

    public void removeEntity(Entity entity)
    {
        entities.remove(entity);
        existingTypes.removeType(entity);
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

    public ArrayList<MultiAssociation> getMultiAssociations()
    {
        return multiAssociations;
    }

    public MultiAssociation getMultiAssociation(String id)
    {
        return multiAssociations.stream().filter(m -> m.getId().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("MultiAssociation not found"));
    }

    public void addInner(Inner i){
        inners.add(i);
    }

    public Inner getInner(String id){
        return inners.stream().filter(i -> i.getId().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("Inner not found"));
    }

    public void removeInner(Inner i){
        inners.remove(i);
    }

    public void addMultiAssociation(MultiAssociation multiAssociation)
    {
        multiAssociations.add(multiAssociation);
    }

    public void removeMultiAssociation(MultiAssociation multiAssociation)
    {
        multiAssociations.remove(multiAssociation);
    }

    public void addAssociationClass(AssociationClass associationClass)
    {
        associationClasses.add(associationClass);
    }

    public AssociationClass getAssociationClass(String id)
    {
        return associationClasses.stream().filter(a -> a.getId().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("AssociationClass not found"));
    }

    public void removeAssociationClass(AssociationClass associationClass)
    {
        associationClasses.remove(associationClass);
    }

    public ArrayList<AssociationClass> getAssociationClasses()
    {
        return associationClasses;
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

        for(Inner i : inners){
            commands.add(i.getCreationCommands());
        }

        for(MultiAssociation m : multiAssociations){
            commands.addAll(m.getCreationCommands());
        }

        for(AssociationClass a : associationClasses){
            commands.addAll(a.getCreationsCommand());
        }

        return commands;
    }


    public void load()
    {
        for (Entity e : entities)
        {
            existingTypes.addType(e);
        }

        for (Entity e : entities)
        {
            e.load(this);
        }

    }
}
