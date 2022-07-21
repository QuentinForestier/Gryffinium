package uml.entities;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.entities.EntityDto;
import dto.entities.EnumDto;
import dto.entities.variables.ValueDto;
import play.libs.Json;
import uml.ClassDiagram;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "Enum")
public class Enum extends ConstructableEntity
{

    private List<String> values;

    public Enum()
    {
        super();
        values = new ArrayList<>();
    }

    public Enum(String name)
    {
        super(name);
        values = new ArrayList<>();
    }


    public Enum(EnumDto ge)
    {
        super(ge);
        values = new ArrayList<>();
        fromDto(ge);
    }

    @XmlElement(name="value")
    public List<String> getValues()
    {
        return values;
    }

    public void setValues(List<String> values)
    {
        this.values = values;
    }

    private boolean isValueExisting(String name)
    {
        return values.stream().anyMatch(s -> s.equals(name));
    }

    public void addValue(String name)
    {
        if (isValueExisting(name))
        {
            throw new RuntimeException("Enum value already exist");
        }
        values.add(name);
    }

    public void updateValue(String currentName, String newName)
    {
        if (!isValueExisting(currentName))
        {
            throw new RuntimeException("Enum value does not exist");
        }
        int index = values.indexOf(currentName);
        values.set(index, newName);
    }

    public void removeValue(String name)
    {
        if (!isValueExisting(name))
        {
            throw new RuntimeException("Enum value does not exist");
        }
        values.remove(name);
    }

    @Override
    public EntityDto toDto()
    {
        return new EnumDto(this);
    }

    @Override
    public ArrayNode getCreationCommands()
    {
        ArrayNode result = Json.newArray();
        result.add(Command.createResponse(toDto(), ElementTypeDto.ENUM));
        result.addAll(getConstructorsCreationCommands());
        result.addAll(getMethodsCreationCommands());
        result.addAll(getAttributesCreationCommands());
        for(String value : values)
        {
            ValueDto ev = new ValueDto();
            ev.setValue(value);
            ev.setParentId(getId());
            result.add(Command.createResponse(ev, ElementTypeDto.VALUE));
        }
        return result;
    }

}
