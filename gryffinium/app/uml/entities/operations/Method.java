package uml.entities.operations;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.entities.operations.MethodDto;
import dto.entities.operations.OperationDto;
import play.libs.Json;
import uml.ClassDiagram;
import uml.entities.Entity;
import uml.types.SimpleType;
import uml.types.Type;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Method extends Operation
{
    private boolean isAbstract;
    private boolean isStatic;

    private Type returnType = null;

    public Method(String name, boolean isAbstract, boolean isStatic){
        super(name);
        this.isAbstract =  isAbstract;
        this.isStatic = isStatic;
    }

    public Method(String name)
    {
        this(name, false, false);
    }


    public Method(dto.entities.operations.MethodDto gm, ClassDiagram cd){
        super(gm, cd);
        if(gm.isAbstract() == null)
        {
            throw new IllegalArgumentException("isAbstract attribute is null");
        }
        if(gm.isStatic() == null)
        {
            throw new IllegalArgumentException("isStatic attribute is null");
        }
        setGraphical(gm, cd);
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

    @XmlAttribute
    public boolean isStatic()
    {
        return isStatic;
    }

    public void setStatic(boolean aStatic)
    {
        isStatic = aStatic;
    }

    @XmlElement
    public Type getReturnType()
    {
        return returnType;
    }

    public void setReturnType(Type returnType)
    {
        this.returnType = returnType;
    }

    public void setGraphical(dto.entities.operations.MethodDto gm, ClassDiagram cd)
    {
        super.setGraphical(gm, cd);
        if(gm.isAbstract() != null)
            this.setAbstract(gm.isAbstract());
        if(gm.isStatic() != null)
            this.setStatic(gm.isStatic());
        if(gm.getType() != null)
        {
            this.returnType = cd.getExistingTypes().getTypeByName(gm.getType());
            if(returnType == null){
                this.returnType = new SimpleType(gm.getType());
                cd.getExistingTypes().addType(this.returnType);
            }
        }
    }


    @Override
    public OperationDto toDto(Entity e)
    {
        return new MethodDto(this, e);
    };

    @Override
    public ArrayNode getCreationCommand(Entity e)
    {
        ArrayNode result = Json.newArray();
        result.add(Command.createResponse(toDto(e), ElementTypeDto.METHOD));
        result.addAll(getParametersCreationCommands(e));
        return result;
    }
}
