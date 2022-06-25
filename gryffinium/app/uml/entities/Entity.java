package uml.entities;

import graphical.entities.GraphicalEntity;
import graphical.entities.variables.GraphicalAttribute;
import uml.Visibility;
import uml.entities.operations.Method;
import uml.entities.operations.Operation;
import uml.entities.variables.Attribute;
import uml.types.Type;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;


public abstract class Entity extends Type
{

    protected int idCounter = 1;

    private Integer id;

    private int x;

    private int y;

    private int width;

    private int height;

    private Visibility visibility;

    private final ArrayList<Attribute> attributs = new ArrayList<>();

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

    public Entity(GraphicalEntity ge)
    {
        super(ge.getName() == null ? "" : ge.getName());

        if(ge.getVisibility() == null)
        {
            this.setVisibility(Visibility.PUBLIC);
        }
        if(ge.getX() == null)
        {
            throw new IllegalArgumentException("X argument missing");
        }
        if(ge.getY() == null)
        {
            throw new IllegalArgumentException("Y argument missing");
        }
        if(ge.getWidth() == null)
        {
            throw new IllegalArgumentException("Width argument missing");
        }
        if(ge.getHeight() == null)
        {
            throw new IllegalArgumentException("Height argument missing");
        }

        setGraphical(ge);
    }


    @XmlAttribute
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @XmlAttribute
    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    @XmlAttribute
    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    @XmlAttribute
    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    @XmlAttribute
    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    @XmlAttribute
    public Visibility getVisibility()
    {
        return visibility;
    }

    public void setVisibility(Visibility visibility)
    {
        this.visibility = visibility;
    }

    public void setGraphical(GraphicalEntity ge)
    {
        if(ge.getName() != null)
            this.setName(ge.getName());

        if(ge.getX() != null)
            this.setX(ge.getX());

        if(ge.getY() != null)
            this.setY(ge.getY());

        if(ge.getWidth() != null)
            this.setWidth(ge.getWidth());

        if(ge.getHeight() != null)
            this.setHeight(ge.getHeight());

        if(ge.getVisibility() != null)
            this.setVisibility(Visibility.valueOf(ge.getVisibility()));
    }

    public Attribute getAttributeById(Integer id){
        return attributs.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    public void addAttribute(Attribute attribute)
    {
        attribute.setId(idCounter++);
        attributs.add(attribute);
    }

    public Operation getOperationById(Integer id)
    {
        return getMethodById(id);
    }

    public Method getMethodById(Integer id){
        return methods.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    public void addMethod(Method method)
    {
        method.setId(idCounter++);
        methods.add(method);
    }
}
