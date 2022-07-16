package uml.types;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import play.libs.Json;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Arrays;

public class ExistingTypes
{

    @XmlElement
    private final ArrayList<Type> existingTypes;

    public ExistingTypes()
    {
        existingTypes = new ArrayList(Arrays.asList(
                PrimitiveType.BOOLEAN_TYPE,
                PrimitiveType.BYTE_TYPE,
                PrimitiveType.SHORT_TYPE,
                PrimitiveType.INT_TYPE,
                PrimitiveType.LONG_TYPE,
                PrimitiveType.FLOAT_TYPE,
                PrimitiveType.DOUBLE_TYPE,
                PrimitiveType.CHAR_TYPE,
                PrimitiveType.STRING_TYPE,
                PrimitiveType.VOID_TYPE));
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
        return existingTypes.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(new SimpleType(name));
    }

}
