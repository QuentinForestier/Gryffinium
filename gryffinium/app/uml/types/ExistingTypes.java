package uml.types;

import java.util.ArrayList;

public class ExistingTypes
{
    private static final ExistingTypes instance = new ExistingTypes();

    private static final ArrayList<Type> existingTypes = new ArrayList<>();

    private ExistingTypes()
    {

    }

    public static ExistingTypes getInstance()
    {
        return instance;
    }

    public static boolean isTypeExisting(Type type)
    {
        return existingTypes.stream().anyMatch(t -> t.getName().equals(type.getName()));
    }

    public static void addType(Type type)
    {
        if (isTypeExisting(type))
        {
            throw new RuntimeException("Type already exist");
        }
        existingTypes.add(type);

        System.out.println("Type " + type.getName() + " added");
    }

    public static Type getTypeByName(String name)
    {
        return existingTypes.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }
}
