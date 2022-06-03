package uml.entities;

import uml.Visibility;
import uml.entities.operations.Method;
import uml.entities.variables.Attribut;
import uml.types.Type;

import java.util.ArrayList;

public abstract class Entity extends Type
{
    private int x;
    private int y;
    private int width;
    private int height;

    private Visibility visibility;

    private final ArrayList<Attribut> attributs = new ArrayList<>();

    private final ArrayList<Method> methods = new ArrayList<>();

    public Entity(String name, Visibility visibility)
    {
        super(name);
        this.visibility = visibility;
    }

    public Entity(String name)
    {
        this(name, Visibility.PUBLIC);
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public Visibility getVisibility()
    {
        return visibility;
    }

    public void setVisibility(Visibility visibility)
    {
        this.visibility = visibility;
    }
}
