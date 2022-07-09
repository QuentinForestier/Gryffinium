package uml.entities.variables;

import com.fasterxml.jackson.databind.node.ArrayNode;
import dto.entities.variables.VariableDto;
import uml.entities.Entity;
import uml.types.Type;
import uml.types.SimpleType;
import uml.ClassDiagram;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


public abstract class Variable
{

    static int idCounter = 0;

    private Integer id;
    private String name;
    private boolean isConstant;

    private Type type = null;

    public Variable(String name, boolean isConstant){
        this.name = name;
        this.isConstant = isConstant;
        this.id = idCounter++;
    }

    public Variable(String name){
        this(name, false);
    }

    public Variable(dto.entities.variables.VariableDto gv, ClassDiagram cd)
    {
        setGraphical(gv, cd);
        this.id = idCounter++;
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

    @XmlElement
    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public void setGraphical(dto.entities.variables.VariableDto gv, ClassDiagram cd)
    {
        if(gv.getId() != null)
            this.setId(gv.getId());
        if(gv.getName() != null)
            this.name = gv.getName();
        if(gv.isConstant() != null)
            this.isConstant = gv.isConstant();
        if(gv.getType() != null){
            this.type = cd.getExistingTypes().getTypeByName(gv.getType());
            if(type == null){
                this.type = new SimpleType(gv.getType());
                cd.getExistingTypes().addType(this.type);
            }
        }
    }


}
