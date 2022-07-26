package uml.entities;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.CommandType;
import dto.ElementTypeDto;
import dto.entities.ClassDto;
import dto.entities.EntityDto;
import play.libs.Json;
import uml.AssociationClass;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlSeeAlso({AssociationClass.class, InnerClass.class})
@XmlType(name="Class")
public class Class extends ConstructableEntity implements Implementor
{
    private boolean isAbstract;

    public Class(){
        super();
    }

    public Class(String name, boolean isAbstract)
    {
        super(name);
        this.isAbstract = isAbstract;
    }

    public Class(String name)
    {
        this(name, false);
    }

    public Class(Class other){
        this.setId(other.getId());
        this.setName(other.getName());
        this.setAbstract(other.isAbstract);
        this.setHeight(other.getHeight());
        this.setWidth(other.getWidth());
        this.setX(other.getX());
        this.setY(other.getY());
        this.setVisibility(other.getVisibility());
    }

    public Class(ClassDto gc)
    {
        super(gc);
        if (gc.isAbstract() == null)
        {
            this.setAbstract(false);
        }
        fromDto(gc);
    }

    @XmlAttribute
    public boolean isAbstract()
    {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract)
    {
        isAbstract = anAbstract;
    }

    public void fromDto(ClassDto ge)
    {
        super.fromDto(ge);
        if (ge.isAbstract() != null)
            this.setAbstract(ge.isAbstract());
    }


    public EntityDto toDto()
    {

        return new ClassDto(this);
    }

    @Override
    public ArrayNode getCreationCommands()
    {
        ArrayNode result = Json.newArray();
        result.add(Command.createResponse(toDto(), ElementTypeDto.CLASS, CommandType.SELECT_COMMAND));
        result.addAll(getConstructorsCreationCommands());
        result.addAll(getMethodsCreationCommands());
        result.addAll(getAttributesCreationCommands());
        return result;
    }
}

