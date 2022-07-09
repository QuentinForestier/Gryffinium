package uml.types;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;

public class ExistingTypes
{

    @XmlElement
    private final ArrayList<Type> existingTypes;

    public ExistingTypes()
    {
        existingTypes = new ArrayList<>();
    }


    public boolean isTypeExisting(Type type)
    {
        return existingTypes.stream().anyMatch(t -> t.getName().equals(type.getName()));
    }

    public void addType(Type type)
    {
        if (isTypeExisting(type))
        {
            throw new RuntimeException("Type already exist");
        }
        existingTypes.add(type);

    }

    public void removeType(Type type)
    {
        existingTypes.remove(type);
    }

    public Type getTypeByName(String name)
    {
        return existingTypes.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }
}
