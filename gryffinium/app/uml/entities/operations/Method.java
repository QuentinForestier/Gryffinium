package uml.entities.operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.CommandType;
import dto.ElementTypeDto;
import dto.entities.operations.MethodDto;
import dto.entities.operations.OperationDto;
import play.libs.Json;
import uml.ClassDiagram;
import uml.entities.Entity;
import uml.entities.Subscribers;
import uml.types.Type;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

public class Method extends Operation implements Subscribers
{
    private boolean isAbstract;
    private boolean isStatic;

    private Type returnType = null;

    private String _returnTypeName;

    public Method()
    {
        super();
    }

    public Method(String name, boolean isAbstract, boolean isStatic)
    {
        super(name);
        this.isAbstract = isAbstract;
        this.isStatic = isStatic;
    }

    public Method(String name)
    {
        this(name, false, false);
    }


    public Method(dto.entities.operations.MethodDto gm, ClassDiagram cd)
    {
        super(gm, cd);
        if (gm.isAbstract() == null)
        {
            throw new IllegalArgumentException("isAbstract attribute is null");
        }
        if (gm.isStatic() == null)
        {
            throw new IllegalArgumentException("isStatic attribute is null");
        }
        if (gm.getParentId() == null)
        {
            throw new IllegalArgumentException("Method doesnt have a parent");
        }

        fromDto(gm, cd);
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

    @XmlTransient
    public Type getType()
    {
        return returnType;
    }

    public void setType(Type returnType)
    {
        if (this.returnType != null)
        {
            this.returnType.unsubscribe(this);
        }
        this.returnType = returnType;
        if (this.returnType != null)
        {
            this.returnType.subscribe(this);
        }
    }

    public void fromDto(MethodDto gm,
                        ClassDiagram cd)
    {
        super.fromDto(gm, cd);
        if (gm.isAbstract() != null)
            this.setAbstract(gm.isAbstract());
        if (gm.isStatic() != null)
            this.setStatic(gm.isStatic());
        if (gm.getType() != null)
        {
            setType(cd.getExistingTypes().getTypeByName(gm.getType()));
        }

        if (gm.getParentId() != null)
        {
            this.setParent(cd.getEntity(gm.getParentId()));
        }

    }


    @Override
    public OperationDto toDto(Entity e)
    {
        if (getParent() == null && e != null)
        {
            setParent(e);
        }
        return new MethodDto(this, e);
    }

    @Override
    public ArrayNode getCreationCommand(Entity e)
    {
        ArrayNode result = Json.newArray();
        result.add(Command.createResponse(toDto(e), ElementTypeDto.METHOD, CommandType.SELECT_COMMAND));
        result.addAll(getParametersCreationCommands(e));
        return result;
    }

    @XmlAttribute(name = "type")
    public String get_returnTypeName()
    {
        _returnTypeName = returnType.getName();
        return _returnTypeName;
    }

    public void set_returnTypeName(String _returnTypeName)
    {
        this._returnTypeName = _returnTypeName;
    }

    public void load(ClassDiagram cd)
    {
        setType(cd.getExistingTypes().getTypeByName(_returnTypeName));
        super.load(cd);
    }

    @Override
    public JsonNode getUpdateNameCommand()
    {
        MethodDto dto = new MethodDto();
        dto.setType(getType().getName());
        dto.setId(getId());
        dto.setParentId(getParent().getId());
        return Command.createResponse(dto, ElementTypeDto.METHOD, CommandType.UPDATE_COMMAND);
    }
}
