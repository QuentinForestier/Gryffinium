package uml.entities;

import java.util.ArrayList;

public class Enum extends ConstructableEntity
{
    private final ArrayList<String> values = new ArrayList<>();

    public Enum(String name)
    {
        super(name);
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
}
