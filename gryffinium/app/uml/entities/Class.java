package uml.entities;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.entities.ClassDto;
import dto.entities.EntityDto;
import play.libs.Json;
import uml.ClassDiagram;

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


    public Class(ClassDto gc, ClassDiagram cd)
    {
        super(gc, cd);
        if (gc.isAbstract() == null)
        {
            this.setAbstract(false);
        }
        fromDto(gc, cd);
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

    public void fromDto(ClassDto ge, ClassDiagram cd)
    {
        super.fromDto(ge, cd);
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
        result.add(Command.createResponse(toDto(), ElementTypeDto.CLASS));
        result.addAll(getConstructorsCreationCommands());
        result.addAll(getMethodsCreationCommands());
        result.addAll(getAttributesCreationCommands());
        return result;
    }
}

