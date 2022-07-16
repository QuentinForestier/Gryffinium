package uml.entities.variables;

import com.fasterxml.jackson.databind.JsonNode;
import dto.entities.variables.VariableDto;
import tyrex.services.UUID;
import uml.entities.Entity;
import uml.entities.Subscribers;
import uml.entities.operations.Operation;
import uml.types.Type;
import uml.types.SimpleType;
import uml.ClassDiagram;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;


public abstract class Variable implements Subscribers
{


    private String id;
    private String name;
    private boolean isConstant;

    private String _typeName;

    private Type type = null;

    private Entity parent;



    public Variable(){

    }

    public Variable(String name, boolean isConstant){
        this.name = name;
        this.isConstant = isConstant;
        this.id = UUID.create();
    }

    public Variable(String name){
        this(name, false);
    }

    public Variable(VariableDto gv, ClassDiagram cd)
    {
        if(gv.getParentId() == null)
        {
            throw new IllegalArgumentException("Variable doesnt have a parent");
        }
        fromDto(gv, cd);
        this.id = UUID.create();
    }


    @XmlTransient
    public Entity getParent()
    {
        return parent;
    }

    public void setParent(Entity parent)
    {
        this.parent = parent;
    }

    @XmlID
    @XmlAttribute
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @XmlAttribute
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlAttribute
    public boolean isConstant()
    {
        return isConstant;
    }

    public void setConstant(boolean constant)
    {
        isConstant = constant;
    }

    @XmlTransient
    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        if(this.type != null)
        {
            this.type.unsubscribe(this);
        }
        this.type = type;
        if(this.type != null)
        {
            this.type.subscribe(this);
        }
    }

    public void fromDto(VariableDto gv, ClassDiagram cd)
    {
        if(gv.getId() != null)
            this.setId(gv.getId());
        if(gv.getName() != null)
            this.name = gv.getName();
        if(gv.isConstant() != null)
            this.isConstant = gv.isConstant();
        if(gv.getType() != null){
            setType(cd.getExistingTypes().getTypeByName(gv.getType()));
        }
        if(gv.getParentId() != null)
            this.parent = cd.getEntity(gv.getParentId());
    }

    @XmlAttribute(name="type")
    public String get_typeName()
    {
        _typeName = type.getName();
        return _typeName;
    }

    public void set_typeName(String _typeName)
    {
        this._typeName = _typeName;
    }

    public void load(ClassDiagram cd){
        setType(cd.getExistingTypes().getTypeByName(_typeName));
    }

}
