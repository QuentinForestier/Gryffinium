package uml.entities;

import graphical.entities.GraphicalEnum;

import java.util.ArrayList;
import java.util.List;

public class Enum extends ConstructableEntity
{
    public List<String> getValues()
    {
        return values;
    }

    public void setValues(List<String> values)
    {
        this.values = values;
    }

    private List<String> values;

    public Enum(String name)
    {
        super(name);
        values = new ArrayList<>();
    }

    public Enum(GraphicalEnum ge)
    {
        super(ge);
        values = new ArrayList<>();
        setGraphical(ge);
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


    public void setGraphical(GraphicalEnum ge)
    {
        super.setGraphical(ge);
        if (ge.getValues() != null)
            this.setValues(ge.getValues());
    }

}
