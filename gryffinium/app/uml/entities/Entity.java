package uml.entities;

import com.fasterxml.jackson.databind.node.ArrayNode;
import dto.entities.EntityDto;
import play.libs.Json;
import tyrex.services.UUID;
import uml.ClassDiagram;
import uml.Visibility;
import uml.entities.operations.Method;
import uml.entities.operations.Operation;
import uml.entities.variables.Attribute;
import uml.types.Type;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;

@XmlSeeAlso({ConstructableEntity.class, Interface.class})
public abstract class Entity extends Type
{

    @XmlID
    @XmlAttribute
    public String getId()
    {
        return id;
    }

    @XmlAttribute
    public int getX()
    {
        return x;
    }

    @XmlAttribute
    public int getY()
    {
        return y;
    }

    @XmlElement(name="attribute")
    public ArrayList<Attribute> getAttributes()
    {
        return attributes;
    }

    private String id;

    private int x;

    private int y;

    private int width;

    private int height;

    private Visibility visibility;


    private final ArrayList<Attribute> attributes = new ArrayList<>();


    private final ArrayList<Method> methods = new ArrayList<>();

    public Entity(){
        this("");
    }

    public Entity(String name, Visibility visibility)
    {
        super(name);
        this.id = UUID.create();
        this.visibility = visibility;
    }

    public Entity(String name)
    {
        this(name, Visibility.PUBLIC);
    }

    public Entity(EntityDto ge)
    {
        super(ge.getName() == null ? "" : ge.getName());

        this.id = UUID.create();

        if (ge.getVisibility() == null)
        {
            this.setVisibility(Visibility.PUBLIC);
        }
        if (ge.getX() == null)
        {
            throw new IllegalArgumentException("X argument missing");
        }
        if (ge.getY() == null)
        {
            throw new IllegalArgumentException("Y argument missing");
        }
        if (ge.getWidth() == null)
        {
            throw new IllegalArgumentException("Width argument missing");
        }
        if (ge.getHeight() == null)
        {
            throw new IllegalArgumentException("Height argument missing");
        }

        fromDto(ge);
    }




    public void setId(String id)
    {
        this.id = id;
    }



    public void setX(int x)
    {
        this.x = x;
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

    public void fromDto(EntityDto ge)
    {
        if (ge.getName() != null)
            this.setName(ge.getName());

        if (ge.getX() != null)
            this.setX(ge.getX());

        if (ge.getY() != null)
            this.setY(ge.getY());

        if (ge.getWidth() != null)
            this.setWidth(ge.getWidth());

        if (ge.getHeight() != null)
            this.setHeight(ge.getHeight());

        if (ge.getVisibility() != null)
            this.setVisibility(Visibility.valueOf(ge.getVisibility().toUpperCase()));
    }


    public Attribute getAttribute(String id)
    {
        return attributes.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }



    public void addAttribute(Attribute attribute)
    {
        attributes.add(attribute);
    }

    public void removeAttribute(Attribute attribute)
    {
        attributes.remove(attribute);
    }


    public Operation getOperationById(String id)
    {
        return getMethodById(id);
    }

    public Method getMethodById(String id)
    {
        return methods.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    @XmlElement(name = "method")
    public ArrayList<Method> getMethods()
    {
        return methods;
    }

    public void addMethod(Method method)
    {
        methods.add(method);
    }

    public void removeMethod(Method method)
    {
        methods.remove(method);
    }

    public abstract EntityDto toDto();

    public abstract ArrayNode getCreationCommands();

    public ArrayNode getMethodsCreationCommands(){
        ArrayNode commands = Json.newArray();
        for (Method method : methods) {
            commands.addAll(method.getCreationCommand(this));
        }
        return commands;
    }

    public ArrayNode getAttributesCreationCommands(){
        ArrayNode commands = Json.newArray();
        for (Attribute attribute : attributes) {
            commands.add(attribute.getCreationCommand(this));
        }
        return commands;
    }

    public void load(ClassDiagram cd){
        for (Method method : methods) {
            method.load(cd);
        }
        for (Attribute attribute : attributes) {
            attribute.load(cd);
        }
    }


}
