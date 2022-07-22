package uml.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.xml.bind.AnyTypeAdapter;
import commands.Command;
import commands.CommandType;
import dto.ElementTypeDto;
import dto.links.InnerDto;
import dto.links.LinkDto;
import uml.ClassDiagram;
import uml.entities.*;
import uml.entities.Class;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlType(name = "Inner")
public class Inner extends Link
{
    private Entity outer;
    private InnerEntity inner;

    public Inner(){
    super();
    }

    public Inner(Entity outer, InnerEntity inner)
    {
        super();
        this.outer = outer;
        this.inner = inner;
    }

    public Inner(InnerDto dto, ClassDiagram cd)
    {
        super();
        fromDto(dto, cd);
    }

    @XmlIDREF
    public Entity getOuter()
    {
        return outer;
    }

    public void setOuter(Entity outer)
    {
        this.outer = outer;
    }

    @XmlIDREF
    @XmlJavaTypeAdapter(AnyTypeAdapter.class)
    public InnerEntity getInner()
    {
        return inner;
    }

    public void setInner(InnerEntity inner)
    {
        this.inner = inner;
    }

    @Override
    public void fromDto(LinkDto dto, ClassDiagram cd)
    {
        super.fromDto(dto, cd);
        if (dto.getSourceId() != null)
        {
            this.inner = convertToInnerEntity(cd.getEntity(dto.getSourceId()));
            cd.removeEntity(cd.getEntity(dto.getSourceId()));
            cd.addEntity((Entity)this.inner);
        }
        if (dto.getTargetId() != null)
        {
            this.outer = cd.getEntity(dto.getTargetId());
        }
    }

    public InnerEntity convertToInnerEntity(Entity entity){
        if(entity instanceof Interface){
            return new InnerInterface((Interface)entity);
        }
        else if(entity instanceof Class){
            return new InnerClass((Class)entity);
        }
        else{
            throw new IllegalArgumentException(entity.getName() + " cannot be an inner entity");
        }
    }

    public Entity convertToEntity(InnerEntity innerEntity){
        if(innerEntity instanceof InnerInterface){
            return new Interface((InnerInterface)innerEntity);
        }
        else if(innerEntity instanceof InnerClass){
            return new Class((InnerClass)innerEntity);
        }
        else{
            throw new IllegalArgumentException("This inner entity cannot be an entity");
        }
    }

    @Override
    public JsonNode getCreationCommands()
    {
        return Command.createResponse(toDto(),
                ElementTypeDto.INNER, CommandType.SELECT_COMMAND);
    }

    @Override
    public LinkDto toDto()
    {
        return new InnerDto(this);
    }
}
