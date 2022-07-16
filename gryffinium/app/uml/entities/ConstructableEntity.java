package uml.entities;

import com.fasterxml.jackson.databind.node.ArrayNode;
import dto.entities.EntityDto;
import play.libs.Json;
import uml.ClassDiagram;
import uml.entities.operations.Constructor;
import uml.entities.operations.Operation;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.List;

@XmlSeeAlso({Enum.class, Class.class})
public abstract class ConstructableEntity extends Entity
{
    @XmlElement(name = "constructor")
    private final ArrayList<Constructor> constructors = new ArrayList<>();

    public ConstructableEntity(){
        super();
    }

    public ConstructableEntity(String name)
    {
        super(name);
    }

    public ConstructableEntity(EntityDto ge, ClassDiagram cd)
    {
        super(ge, cd);
    }

    public void addConstructor(Constructor constructor)
    {
        constructor.setName(getName());
        constructors.add(constructor);
    }

    public void removeConstructor(Constructor constructor)
    {
        constructors.remove(constructor);
    }


    public Constructor getConstructorById(String id)
    {
        return constructors.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }


    public List<Constructor> getConstructors()
    {
        return constructors;
    }


    @Override
    public Operation getOperationById(String id)
    {
        Operation result = super.getOperationById(id);

        return result == null ? getConstructorById(id) : result;
    }

    protected ArrayNode getConstructorsCreationCommands(){
        ArrayNode result = Json.newArray();
        for (Constructor constructor : constructors)
        {
            result.addAll(constructor.getCreationCommand(this));
        }
        return result;
    }
}
